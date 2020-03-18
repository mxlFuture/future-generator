
var active;

layui.use(['layer', 'element'], function() {


	   //iframe自适应
	   function resize(){
	       var $content = $('.admin-nav-card .layui-tab-content');
	       $content.height($(this).height() - 147);
	       $content.find('iframe').each(function() {
	           $(this).height($content.height());
	       });
	   }
	   $(window).on('resize', function() {
	       var $content = $('.admin-nav-card .layui-tab-content');
	       $content.height($(this).height() - 147);
	       $content.find('iframe').each(function() {
	           $(this).height($content.height());
	       });
	   }).resize();


});