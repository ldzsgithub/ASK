/**
 * Ajax请求方法二次封装
 */
var common_ajax = function () {

    /**
     * ajax请求并返回结果
     * @param url
     * @param data
     * @param callback
     */
    var defaultFunc = function (url, data, success, error) {
        var result = "";
        $.ajax({
            type: "post",
            url: url,
            data: data,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            async: false,
            cache: false,
            success: function (response) {
                result = response;
                //扩展回调函数
                if (success != null) {
                    if (response.code == 0) {
                        success(response);
                    } else {
                        if (error != null) {
                            error(response);
                        }
                        layer.msg(response.msg);
                    }
                }
            }
        });
        return result;
    }

    /**
     * ajax请求并返回结果
     * @param url
     * @param data
     * @param callback
     */
    var defaultFileFunc = function (url, data, success, error) {
        var result = "";
        $.ajax({
            type: "post",
            url: url,
            data: data,
            async: false,
            cache: false,
            contentType: false, //禁止设置请求类型
            processData: false, //禁止jquery对DAta数据的处理,默认会处理
            //禁止的原因是,FormData已经帮我们做了处理
            success: function (response) {
                result = response;
                //扩展回调函数
                if (success != null) {
                    if (response.code == 0) {
                        success(response);
                    } else {
                        if (error != null) {
                            error(response);
                        }
                        layer.msg(response.msg);
                    }
                }
            }
        });
        return result;
    }

    var ajaxFunc = function (url, data, dataType,success, error) {
        var result = "";
        $.ajax({
            type: "post",
            url: url,
            data: data,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            async: false,
            cache: false,
            success: function (response) {
                result = response;
                //扩展回调函数
                if (success != null) {
                    if (response.code == 0) {
                        success(response);
                    } else {
                        if (error != null) {
                            error(response);
                        }
                        layer.msg(response.msg);
                    }
                }
            }
        });
        return result;
    }

    var deleteFunc = function (url, data, success) {
        layer.confirm("您确定要删除么？", {
            btn: ['是', '否'] //按钮
        }, function () {
            defaultFunc(
                url,
                data,
                function (response) {
                    success(response);
                }
            );
        });
    }
    return {
        defaultFunc: defaultFunc,
        defaultFileFunc:defaultFileFunc,
        deleteFunc: deleteFunc
    };

}();
