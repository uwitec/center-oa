/*
 * Compressed by JSA(www.xidea.org)
 */
var interval=500,mouseClick1=0,mouseClick2=0;function listenerMouse($,_){if($.button==2){if(mouseClick2==0){mouseClick2=new Date().getTime();return}if(mouseClick1==0){mouseClick1=new Date().getTime();cgoBack(mouseClick1,mouseClick2,_);return}mouseClick2=mouseClick1;mouseClick1=new Date().getTime();cgoBack(mouseClick1,mouseClick2,_);return}else{mouseClick1=0;mouseClick2=0}}function cgoBack(A,$,_){if(A-$<interval)if(_){if(typeof _=="function")_.call(this);if(typeof _=="string")document.location.href=_}else window.history.go(-1)}function getEvent(){if(window.event)return window.event;var _=this.getEvent;while(_!=null){var $=_.arguments.callee.arguments[0];if($!=null&&$.constructor&&$.constructor.toString().indexOf("Event")!=-1)return $;_=_.caller}return null}document.onmouseup=function(){if(document.body.onmouseup==null){var $=getEvent();listenerMouse($)}};document.oncontextmenu=function(){if(document.body.oncontextmenu==null)return false};document.onkeydown=function(){if(document.body.onkeydown==null){var $=getEvent();if($.keyCode==27){window.history.go(-1);return false}}}