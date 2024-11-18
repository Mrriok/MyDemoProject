package com.zony.common.filenet.util;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import javax.security.auth.Subject;


/**
 * Authorized object store contains a object store and the subject used to fetch
 * it. To invoke any following methods which need to make a round-trip to CE
 * server, a subject representing the user who has enough privileges must be
 * pushed into the current thread's UserContext.
 * <p>
 * Methods like <tt>ObjectStore.get_Id()<tt> and <tt>ObjectStore.get_Name()</tt>
 * does not require a subject in the current thread's UserContext.
 * <p>
 * A CE connction and object store does not contains any authorization
 * information. A object store must be combined with a subject to make
 * round-trip CE operations. The combination is via thread. If a object store
 * needs to be combined with a subject in a thread, the subject must be pushed
 * into the thread. A object store can be used with different subjects to have
 * different privileges. A UserContext can contains many subjects. But only the
 * top one is effective. For the details of UserContext, refer to its Javadoc.
 * <p>
 * CE connection and object store are managed with a conneciton pool by CE API.
 */
public class AuthenticatedObjectStore {

	private ObjectStore os;
	private Subject subject;
	
	public static ThreadLocal<ObjectStore> LocalObjectStore=new ThreadLocal<ObjectStore>();

	public static ObjectStore getLocalObjectStore(){
		return LocalObjectStore.get();
	}
	/**
	 * Constructs a authorized object store. The steps are involved in this
	 * method:
	 * <ul>
	 * <li>
	 * configure CE login</li>
	 * <li>push a subject for the given user (specified by the user name and
	 * password) into the current thread's UserContext</li>
	 * <li>fetch a object store if <tt>fetch</tt> is <tt>true</tt>; get a object
	 * store otherwise</li>
	 * </ul>
	 * <b>NOTE</b>: the object store can be used in the same thread as the
	 * invocation of this method. If the object store is used in other threads,
	 * a subject must be pushed into the current thread beforehand.
	 * 
	 * @param userName
	 *            user name
	 * @param password
	 *            password
	 * @param fetch
	 *            to fetch a object store instance; <tt>false</tt> to
	 *            get a object store.
	 */
	public AuthenticatedObjectStore(String userName, String password, boolean fetch) {
		Connection conn = ConnectionUtils.getConnection();
		if (ZonyStringUtil.isNotEmpty(password)) {
			// Password is null in SSO env.
			subject = UserContextUtils.pushSubject(conn, userName, password);
		}
		Domain domain = Factory.Domain.getInstance(conn, null);
		if (fetch) {
			os = Factory.ObjectStore.fetchInstance(domain, FnConfigOptions.getObjectStoreName(), null);
		} else {
			os = Factory.ObjectStore.getInstance(domain, FnConfigOptions.getObjectStoreName());
		}
	}

	/**
	 * Create a default authorizated object store.
	 * 
	 */
	public static AuthenticatedObjectStore createDefault() {
		return new AuthenticatedObjectStore(FnConfigOptions.getUserName(), FnConfigOptions.getPassword(), true);
	}

	/**
	 * Returns the subject which has been pushed into UserContext before the
	 * invocation of <tt>Factory.ObjectStore.fetchInstance</tt>.
	 * 
	 * @return subject
	 */
	public Subject getSubject() {
		return subject;
	}

	/**
	 * Returns the object store.
	 * 
	 * @return object store.
	 */
	public ObjectStore getObjectStore() {
		return os;
	}

}
