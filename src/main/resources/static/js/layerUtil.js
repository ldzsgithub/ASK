/**
 * Ajax请求方法二次封装
 */
var layerUtils = function () {

    /**
     * ajax请求并返回结果
     * @param url
     * @param data
     * @param callback
     */
    var openPage = function (title,width,height,url) {
        layer.open({
            type: 2,
            title:title,
            skin: 'layui-layer-rim', //加上边框
            area: [width,height], //宽高
            content: url
        });
    }

    return {
        openPage: openPage
    };

}();
