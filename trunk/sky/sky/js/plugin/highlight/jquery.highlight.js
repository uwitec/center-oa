/*
 * Compressed by JSA(www.xidea.org)
 */
$(function(){jQuery.highlight=document.body.createTextRange?function(C,B,_){var A=document.body.createTextRange();A.moveToElementText(C);for(var $=0;A.findText(B);$++){A.pasteHTML("<font color=\""+_+"\">"+A.text+"</font>");A.collapse(false)}}:function(E,D,A){var C,B,F,H,G,I;B=0;if(E.nodeType==3){C=E.data.toUpperCase().indexOf(D);if(C>=0){F=document.createElement("span");$(F).css("color",A);H=E.splitText(C);G=H.splitText(D.length);I=H.cloneNode(true);F.appendChild(I);H.parentNode.replaceChild(F,H);B=1}}else if(E.nodeType==1&&E.childNodes&&!/(script|style)/i.test(E.tagName))for(var _=0;_<E.childNodes.length;++_)_+=$.highlight(E.childNodes[_],D,A);return B}});jQuery.fn.removeHighlight=function(){return this.find("span.highlight").each(function(){this.parentNode.replaceChild(this.firstChild,this).normalize()})};function highlights(C,B,A){for(var _=0;_<B.length;_++)$.highlight(C,B[_],A)}