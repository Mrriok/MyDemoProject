
package com.zony.common.service;

import com.zony.common.repository.ServerDeployRepository;
import com.zony.common.service.mapstruct.ServerDeployMapper;
import lombok.RequiredArgsConstructor;
import com.zony.common.domain.ServerDeploy;
import com.zony.common.service.dto.ServerDeployDto;
import com.zony.common.service.criteria.ServerDeployQueryCriteria;
import com.zony.common.utils.ExecuteShellUtil;
import com.zony.common.utils.FileUtil;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ServerDeployService {

    private final ServerDeployRepository serverDeployRepository;
    private final ServerDeployMapper serverDeployMapper;


    public Object queryAll(ServerDeployQueryCriteria criteria, Pageable pageable){
        Page<ServerDeploy> page = serverDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(serverDeployMapper::toDto));
    }


    public List<ServerDeployDto> queryAll(ServerDeployQueryCriteria criteria){
        return serverDeployMapper.toDto(serverDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }


    public ServerDeployDto findById(Long id) {
        ServerDeploy server = serverDeployRepository.findById(id).orElseGet(ServerDeploy::new);
        ValidationUtil.isNull(server.getId(),"ServerDeploy","id",id);
        return serverDeployMapper.toDto(server);
    }


    public ServerDeployDto findByIp(String ip) {
        ServerDeploy deploy = serverDeployRepository.findByIp(ip);
        return serverDeployMapper.toDto(deploy);
    }


	public Boolean testConnect(ServerDeploy resources) {
		ExecuteShellUtil executeShellUtil = null;
		try {
			executeShellUtil = new ExecuteShellUtil(resources.getIp(), resources.getAccount(), resources.getPassword(),resources.getPort());
			return executeShellUtil.execute("ls")==0;
		} catch (Exception e) {
			return false;
		}finally {
			if (executeShellUtil != null) {
				executeShellUtil.close();
			}
		}
	}


    @Transactional(rollbackFor = Exception.class)
    public void create(ServerDeploy resources) {
		serverDeployRepository.save(resources);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(ServerDeploy resources) {
        ServerDeploy serverDeploy = serverDeployRepository.findById(resources.getId()).orElseGet(ServerDeploy::new);
        ValidationUtil.isNull( serverDeploy.getId(),"ServerDeploy","id",resources.getId());
		serverDeploy.copy(resources);
        serverDeployRepository.save(serverDeploy);
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            serverDeployRepository.deleteById(id);
        }
    }


    public void download(List<ServerDeployDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerDeployDto deployDto : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("服务器名称", deployDto.getName());
            map.put("服务器IP", deployDto.getIp());
            map.put("端口", deployDto.getPort());
            map.put("账号", deployDto.getAccount());
            map.put("创建日期", deployDto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
