/**
 * Created by Ray on 2017/03/15.
 */
//var formData = [
//        {
//            id: "inputId3",//<string>当前form元素的id，必需
//            name: "inputName1",//<string>当前form元素的name，必需
//            label: "label1",//<string>当前form元素的label，必需
//            col: 4,//<num>当前form元素所占宽度，非必需，默认12。<0或>12时，取12
//            type: "select",//<string>当前form元素的类型，非必需，默认是input。input：输入框，input-btn：尾部有按钮的输入框，select：下拉，date：时间选择组件，daterange：时间范围选择，textarea：文本域，radio：单选框，checkbox：复选框，hidden：隐藏域，password：密码
//            newLine:true,//<boolean>当前form元素是否另起一行，默认false，注：textarea之后的元素强制true
//            dataField: ["a", "b"],//<list>指定数据源的value，displayName的字段，与dataList中的key对应
//            dataList: [
//                {
//                    a: "a",
//                    b: "一般"
//                }, {
//                    a: "b",
//                    b: "普通",
//                    selected: "true"
//                }, {
//                    a: "c",
//                    b: "紧急"
//                }
//            ],//<list>当前form元素的数据，适应select，radio，checkbox。传入每一个value和dispName，字段用dataField指定，select可以用selected指定默认选中，radio，checkbox可以用checked指定
//            maxLength: 100,//<num>input，input-btn，textarea的输入最大限制
//            anotherId: "inputId5-2",//<string>适用于daterange，指定第二个框的id，类型为daterange时，未指定此项将错误，类型为其他时，此项无效
//            anotherName: "inputName5-2",//<string>适用于daterange，指定第二个框的name，类型为daterange时，未指定此项将错误，类型为其他时，此项无效
//            placeholder: "",//<string>为输入框、文本域等，指定默认显示的值
//            btnLabel:"查询",//<string>为input-btn类型指定button内的文字
//            btnId:"searchBtn",//<string>为input-btn类型指定button的id
//            btnColor:"green"//<string>为input-btn的button指定颜色，适用模板的预设颜色，与正常button一致
//
//        }
//];

var zonyForm = function () {
  function setForm(formData, data) {
    if (data[formData.name] !== undefined) {
      //赋值代码
      if (!formData.type || formData.type == "input" || formData.type == "hidden" || formData.type == "password" || formData.type == "input-btn" || formData.type == "date" || formData.type == "select" || formData.type == "textarea") {
        if (data[formData.name] != null) {
          $("#" + formData.id).val(data[formData.name]).change();
        } else {
          $("#" + formData.id).val("").change();
        }
      } else if (formData.type == "radio") {
        $("#" + formData.id + " input").each(function () {
          if ($(this).val() == data[formData.name]) {
            $(this).prop("checked", true).change();
          } else {
            //$(this).attr("checked", "");
          }
        })
      } else if (formData.type == "checkbox") {
        var dataKey = formData.dataField[0];
        var dataList = formData.dataList;
        for (var i = 0; i < dataList.length; i++) {
          var checkKey = dataList[i][dataKey];
          if (data[formData.name][checkKey]) {
            $("#" + formData.id + " input[value='" + checkKey + "']").prop("checked", true).change();
          } else {
            $("#" + formData.id + " input[value='" + checkKey + "']").prop("checked", false).change();
          }
        }
      }
      return;
    } else {
      for (var key in data) {
        if (typeof (data[key]) == "object" && data[key] != null && isNaN(data[key].length)) {
          setForm(formData, data[key])
        }
      }
    }
  }

  function lengthsMax(num) {
    if (num == 0 || num == 1) {
      return 1;
    } else {
      return 10 * arguments.callee(--num);
    }
  }

  function setFormEx(formData, data, keySet, valueSet) {
    for (var i = 0; i < data.length; i++) {
      if (formData.name == data[i][keySet]) {
        $("#" + formData.id).val(data[i][valueSet] ? data[i][valueSet]: [] ).change();
        return;
      }
    }
  }

  function formatDataList(dataList, valueField, displayField) {
    //将select，checkbox，radio的dataList格式化成标准格式
    var newDataList = [];
    if (valueField) {
      for (var i = 0; i < dataList.length; i++) {
        newDataList.push(dataList[i]);
        for (var key in dataList[i]) {
          if (key == valueField) {
            newDataList[i].value = dataList[i][key];
          }
          if (key == displayField) {
            newDataList[i].displayName = dataList[i][key];
          }
        }
      }
    } else {
      for (var j = 0; j < dataList.length; j++) {
        var obj = {};
        obj.value = dataList[j];
        obj.displayName = dataList[j];
        newDataList.push(obj);
      }
    }
    return newDataList;
  }

  function getForm(item) {
    var itemObj;
    var itemJSON = {};
    if (!item.type || item.type == "input" || item.type == "hidden" || item.type == "password" || item.type == "input-btn" || item.type == "date" || item.type == "textarea") {
      itemObj = $("#" + item.id).serializeArray()[0];
      itemJSON[itemObj.name] = itemObj.value;
      return itemJSON;
    }else if(item.type == "select" ){
      var value = $("#"+ item.id + " option:selected").val()
      itemJSON[item.name] = value;
      return itemJSON;
    } else if (item.type == "radio") {
      itemObj = $("#" + item.id + " input:checked").serializeArray()[0];
      itemJSON[itemObj.name] = itemObj.value;
      return itemJSON;
    } else if (item.type == "checkbox") {
      var checkList = {};
      var checkName = $("#" + item.id).attr("data-name");
      $("#" + item.id + " input").each(function () {
        if ($(this).is(":checked")) {
          itemJSON[$(this).val()] = true;
          checkList = $.extend({}, checkList, itemJSON);
        } else {
          itemJSON[$(this).val()] = false;
          checkList = $.extend({}, checkList, itemJSON);
        }
      });
      var returnObj = {};
      returnObj[checkName] = checkList;
      return returnObj;
    }
  }

  return {
    init: function (divId, itemList, length) {
      var $div = $("#" + divId);
      var labelLength = length ? length : 80;
      $div.append('');
      var itemHTML;
      var item;
      for (var i = 0; i < itemList.length; i++) {
        item = itemList[i];
        var col = (item.col && (item.col <= 12) && (item.col >= 1)) ? item.col : 12;//无长度控制，或控制值大于12栅格时，默认为12
        if (item.blank) {
          $div.append('<div class="blank col-xs-' + col + ' form-div-style" style="'+ item.style +'"></div>');
          continue;
        }
        itemHTML = "";
        itemHTML += item.newLine ? '<div style="clear: both"></div>' : '';
        if (!item.id) {
         console.log("表单初始化失败", "第" + i + 1 + "项未提供合法id");
          continue;
        }
        if (!item.name) {
         console.log("表单初始化失败", "第" + i + 1 + "项未提供合法name");
          continue;
        }
        if (!item.label) {
         console.log("表单初始化失败", "第" + i + 1 + "项未提供合法label");
          continue;
        }
        var rows = item.rows ? item.rows : 2;//判断是否有textarea的行数，默认2
        var placeholder = item.placeholder ? item.placeholder : "";//判断是否有placeholder，默认空字符
        var format = item.format ? item.format : "yyyy-mm-dd";//判断时间格式，默认yyyy-mm-dd
        var dataList = item.dataList ? item.dataList : null;
        if (item.dataField) {
          var valueField = item.dataField[0];
          var displayNameField = item.dataField[1];
          if (dataList) {
            dataList = formatDataList(dataList, valueField, displayNameField);
          }
        }
        var resizeStyle = 'style="resize:none"';
        if (item.resize) {
          if (item.resize == 'v') {
            resizeStyle = 'style="resize:vertical"';
          } else if (item.resize == 'h') {
            resizeStyle = 'style="resize:horizontal"';
          } else if (item.resize == 'b') {
            resizeStyle = 'style="resize:both"';
          }
        }
        //zlk 添加，必填红色星号 start
        var required = item.required == true ? item.required : false;
        if (required) {
          itemHTML += '<div class="col-xs-' + col + ' form-div-style">' +
            '<label class="label-style">  <span class="required" style="color:#e02222"> * </span> <span > ' + item.label + ' </span></label>';
        } else {
          itemHTML += '<div class="col-xs-' + col + ' form-div-style">' +
            '<label class="label-style">' + item.label + '</label>';
        }
        //zlk 添加，必填红色星号 end
        var maxLength = item.maxLength > 0 ? item.maxLength : 0;//判断是否启用最大输入控制，默认0，不控制。负数无效
        var lengthMax = lengthsMax(maxLength) - 1
        if (item.readonly) {
          switch (item.type) {
            case 'textarea':
              itemHTML += '<textarea class="form-control form-input-style" id="' + item.id + '" name="' + item.name + '" rows="' + rows + '" placeholder="' + placeholder + '" ' + resizeStyle + ' readonly></textarea>';
              itemHTML += '</div><div style="clear: both"></div>';
              break;
           
            case 'radio':
              itemHTML += '<div class="input-group form-input-style radio-checkbox-style" style=" pointer-events: none;cursor: default;" id="' + item.id + '">';
              if (dataList) {
                var radioItem = "";
                for (var k = 0; k < dataList.length; k++) {
                  if (dataList[k].checked) {
                    radioItem += '<label><input  readonly type="radio" name="' + item.name + '" value="' + dataList[k].value + '" checked="checked"><span class="radioCircle"><i class="fa fa-circle radioChecked"></i></span>' + dataList[k].displayName + '</label>';
                  } else {
                    radioItem += '<label><input  readonly type="radio" name="' + item.name + '" value="' + dataList[k].value + '"><span class="radioCircle"><i class="fa fa-circle radioChecked"></i></span>' + dataList[k].displayName + '</label>';
                  }
                }
                itemHTML += radioItem;
              }
              itemHTML += '</div></div>';
              break;

            default:
              itemHTML += '<input class="form-control input-sm form-input-style" id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '" readonly></div>';
              break;
          }
        } else {
          switch (item.type) {
            case 'hidden':
              itemHTML = '<input type="hidden" class="form-control input-sm form-input-style" id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '"></div>';
              break;
            case 'password':
              itemHTML += '<input type="password" class="form-control input-sm form-input-style" id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '"></div>';
              break;
            case 'textarea':
              itemHTML += '<textarea class="form-control form-input-style" id="' + item.id + '" name="' + item.name + '" rows="' + rows + '" placeholder="' + placeholder + '" ' + resizeStyle + '></textarea>';
              itemHTML += '</div><div style="clear: both"></div>';
              break;
            case 'select':
              itemHTML += '<select class="form-control input-sm form-input-style" id="' + item.id + '" name="' + item.name + '">';
              if (dataList) {
                var selectItem = "";
                for (var j = 0; j < dataList.length; j++) {
                  if (dataList[j].selected) {
                    selectItem += '<option value="' + dataList[j].value + '"'  +(dataList[j].disabled == true ? 'disabled' : '') + ' selected="selected">' + dataList[j].displayName + '</option>';
                  } else {
                    selectItem += '<option value="' + dataList[j].value + '"' + (dataList[j].disabled == true ? 'disabled' : '') + '>' + dataList[j].displayName + '</option>';
                  }
                }
                itemHTML += selectItem;
              }
              itemHTML += '</select></div>';
              break;
            case 'date':
              itemHTML += '<div class="input-group input-group-sm date date-picker" data-date-format="' + format + '">' +
                '<input type="text" class="form-control form-input-style" id="' + item.id + '" name="' + item.name + '" readonly style="background: white">' +
                '<span class="input-group-btn"><a class="btn default btn-sm" type="button"><i class="fa fa-calendar"></i></a></span></div></div>';
              break;
            case 'input-btn':
              var btnColor = item.btnColor ? item.btnColor : "blue";
              itemHTML += '<div class="input-group input-group-sm">' +
                '<input type="text" class="form-control input-sm form-input-style" readonly style="background: white" id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '">' +
                '<span class="input-group-btn">';
              itemHTML += '<button class="btn ' + btnColor + '" type="button" id="' + item.btnId + '">' + item.btnLabel + '</button></span></div></div>';
              break;
            case 'radio':
              itemHTML += '<div class="input-group form-input-style radio-checkbox-style" id="' + item.id + '">';
              if (dataList) {
                var radioItem = "";
                for (var k = 0; k < dataList.length; k++) {
                  if (dataList[k].checked) {
                    radioItem += '<label><input type="radio" name="' + item.name + '" value="' + dataList[k].value + '" checked="checked"><span class="radioCircle"><i class="fa fa-circle radioChecked"></i></span>' + dataList[k].displayName + '</label>';
                  } else {
                    radioItem += '<label><input type="radio" name="' + item.name + '" value="' + dataList[k].value + '"><span class="radioCircle"><i class="fa fa-circle radioChecked"></i></span>' + dataList[k].displayName + '</label>';
                  }
                }
                itemHTML += radioItem;
              }
              itemHTML += '</div></div>';
              break;
            case 'checkbox':
              itemHTML += '<div class="input-group form-input-style radio-checkbox-style" id="' + item.id + '" data-name="' + item.name + '">';
              if (dataList) {
                var checkboxItem = "";
                for (var l = 0; l < dataList.length; l++) {
                  if (dataList[l].checked) {
                    checkboxItem += '<label><input type="checkbox" name="' + item.name + '" value="' + dataList[l].value + '" checked="checked"><span class="checkSquare"><i class="fa fa-check checkChecked"></i></span>' + dataList[l].displayName + '</label>';
                  } else {
                    checkboxItem += '<label><input type="checkbox" name="' + item.name + '" value="' + dataList[l].value + '"><span class="checkSquare"><i class="fa fa-check checkChecked"></i></span>' + dataList[l].displayName + '</label>';
                  }
                }
                itemHTML += checkboxItem;
              }
              itemHTML += '</div></div>';
              break;
            case 'number':
              itemHTML += '<input class="form-control input-sm form-input-style" type="number" max="' + item.max + '" max="' + item.min + '"  id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '"  value="' + item.value + '"></div>';

              // itemHTML += '<input class="form-control input-sm form-input-style" type="number" id="' + item.id + '" max="' + item.max || undefined + '" min="' + item.min || undefined + '" name="' + item.name + '" placeholder="' + placeholder + '"  value="' + item.value + '" ></div>';
              break;
            default:
              itemHTML += '<input class="form-control input-sm form-input-style" id="' + item.id + '" name="' + item.name + '" placeholder="' + placeholder + '"></div>';
              break;

          }
        }
        $div.append(itemHTML);




      }
      $div.append('<div style="clear: both"></div>');
    },
    setFormData: function (itemList, data) {
      for (var i = 0; i < itemList.length; i++) {
        setForm(itemList[i], data);
      }
    },
    getFormData: function (itemList) {
      var formDataJSON = {};
      for (var i = 0; i < itemList.length; i++) {
        var itemJSON = getForm(itemList[i]);
        formDataJSON = $.extend({}, formDataJSON, itemJSON);
      }
      return formDataJSON;
    },
    //后台数据为{symbolicName:"abc",value:"xyz"}时的赋值
    setFormDataEx: function (itemList, data, keySet, valueSet) {
      for (var i = 0; i < itemList.length; i++) {
        setFormEx(itemList[i], data, keySet, valueSet);
      }
    },
    isNullNew: function (arr) {
      arr = _.reduce(arr, function (result, item, key) {
        if (item.required == true) {
          result.push(item.id)
        }
        return result;
      }, [])
      var isNull = false;
      arr.map(function (item, index) {
        if ($('#' + item).val() === null || $('#' + item).val() === '' || $.trim($('#' + item).val()) === '' || $.trim($('#' + item).val()) === 'null') {
          $('#' + item).addClass('errorInput');
          isNull = true;
          // baas.showWarning(item.name)
        } else {
          $('#' + item).removeClass('errorInput');
        }
      });
      if (isNull == true) {
       console.log("有未完成的输入项！");
      }
      return isNull
    },
    isNull: function (arr) {
      var isNull = true;
      arr.map(function (item, index) {
        if ($('#' + item).val() === null || $('#' + item).val() === '') {
          $('#' + item).addClass('errorInput');
          isNull = false;
          //baas.showWarning(item)
        } else {
          $('#' + item).removeClass('errorInput');
        }
      });
      if (isNull == false) {
       console.log("有未完成的输入项！");
      }
      return isNull
    }
  }
}();
