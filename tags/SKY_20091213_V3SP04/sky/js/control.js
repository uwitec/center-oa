//var BaseUrl = "http://127.0.0.1/soaaspcode/";

//隐藏或显示修订痕迹
function soaShowTrack(value) 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.ShowRevisions = value;
} 
//接受所有修订，清除痕迹
function soaAcceptAllRevisions() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.AcceptAllRevisions();
}
//获取并显示所有痕迹
function GetAllRevisions()
{
  //var i;
  //var str="";
  //for (i = 1;i <=formData.SOAOfficeCtrl.Document.Revisions.Count;i++)
  //{
  //  str=str + formData.SOAOfficeCtrl.Document.Revisions.Item(i).Author;
  //  if (formData.SOAOfficeCtrl.Document.Revisions.Item(i).Type=="1")
  //	{ 
  //  	str=str + ' 插入：'+formData.SOAOfficeCtrl.Document.Revisions.Item(i).Range.Text+"\r\n";
  //  }
  //	else
  //	{
  //  	str=str + ' 删除：'+formData.SOAOfficeCtrl.Document.Revisions.Item(i).Range.Text+"\r\n";
  //  }
  //}
  //alert("当前文档的所有修改痕迹如下：\r\n"+str);

	alert("绿色版没有提供此功能。");
}
//插入本地印章
function soaInsertLocalSeal() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		alert("绿色版没有提供插入印章的功能。");//formData.SOAOfficeCtrl.InsertSealFromLocal();
}
//插入手写签名
function soaInsertSignature() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
	{
		 alert("绿色版不提供手写签名功能。");
	}
}
//全文手写批注
function soaStartHandDraw() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		{
		  alert("绿色版不提供手写批注功能。");
		}
} 
//分层显示全文手写批注
function soaShowHandDrawDispBar() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		{
		  alert("绿色版不提供分层显示手写批注功能。");
		}
} 
//给文档添加数字签名
function soaAddDigitalSignature() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		 alert("绿色版不提供添加数字签名功能。");
} 
//插入电子印章
function soaInsertSeal() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
	{
		alert("绿色版不提供插入印章功能。");
	}
} 
//验证电子印章/签名的有效性
function soaValidateSeal() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		alert("绿色版不提供插入印章功能。");
} 
//允许或禁止 复制/拷贝
function soaCanCopy(value) 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.CanCopy = value;	
	if (value)
		alert("已允许拷贝！");
	else	
		alert("已禁止拷贝！");
} 
//插入Web图片
function InsertWebImage() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		alert("绿色版没有提供此功能。");//formData.SOAOfficeCtrl.InsertWebImage(BaseUrl + "images/SOA_05.gif");
}

//页面设置
function DocPageSetup() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.ShowDialog(5);
}
//切换标题栏
function ToggleTitlebar() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.Titlebar = !formData.SOAOfficeCtrl.Titlebar;
}
//切换菜单栏
function ToggleMenubar() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.Menubar = !formData.SOAOfficeCtrl.Menubar;
}
//切换工具栏
function ToggleToolbars() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.Toolbars = !formData.SOAOfficeCtrl.Toolbars;
}
//禁止/允许 打印文档菜单及按钮
function EnablePrint(value) 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.EnableFileCommand(5) = value;
}
//禁止/允许 保存文档菜单及按钮
function EnableSave(value) 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.EnableFileCommand(3) = value;
}
//禁止/允许 另存文档菜单及按钮
function EnableSaveAs(value) 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		formData.SOAOfficeCtrl.EnableFileCommand(4) = value;
}
//保存文档到web服务器
function soaSave() 
{ 
	try {
		if (!bDocOpen)
			alert("当前没有已打开的文档。");
		else
			formData.SOAOfficeCtrl.WebSave();//保存当前文档到web服务器，保存（覆盖）到原打开文档的地址处
		//formData.SOAOfficeCtrl.WebSave("aa.doc");表示把当前文档另存到web服务器为aa.doc
	} 
	catch (e) 
	{ 
		alert("文档保存失败!\n错误信息:" + e.message); 
	} 
}
//保存文档到web服务器，使用页面提交技术，在提交文档的同时提交其他用户定义的页面字段或域
function soaSubmitSave() 
{ 
	try {
		if (!bDocOpen)
			alert("当前没有已打开的文档。");
		else
		{
			formData.SOAOfficeCtrl.WebSave();
			formData.submit();
		}
	} 
	catch (e) 
	{ 
		alert("文档保存失败!\n错误信息:" + e.message); 
	} 
}
//打开插入本地图片的对话框
function OpenImageDialog() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		alert("绿色版没有提供此功能。");//formData.SOAOfficeCtrl.OpenImageDialog();
} 
//获取文档Txt正文
function WordToText()
{
	alert("绿色版没有提供此功能.");
    //alert(formData.SOAOfficeCtrl.DocText);
}
//VBA套红，套用VBA编程模板
function InsertVBATemplate()
{
	//var DocObject=formData.SOAOfficeCtrl.Document;
	//var myl=DocObject.Shapes.AddLine(91,60,285,60)
	//myl.Line.ForeColor=255;
	//myl.Line.Weight=2;
	//var myl1=DocObject.Shapes.AddLine(308,60,502,60)
	//myl1.Line.ForeColor=255;
	//myl1.Line.Weight=2;

   	//var myRange=DocObject.Range(0,0);
	//myRange.Select();

	//var mtext="★";
	//DocObject.Application.Selection.Range.InsertAfter (mtext+"\n");
   	//var myRange=DocObject.Paragraphs(1).Range;
   	//myRange.ParagraphFormat.LineSpacingRule =1.5;
   	//myRange.font.ColorIndex=6;
   	//myRange.ParagraphFormat.Alignment=1;
   	//myRange=DocObject.Range(0,0);
	//myRange.Select();
	//mtext="市政发[2005]0168号";
	//DocObject.Application.Selection.Range.InsertAfter (mtext+"\n");
	//myRange=DocObject.Paragraphs(1).Range;
	//myRange.ParagraphFormat.LineSpacingRule =1.5;
	//myRange.ParagraphFormat.Alignment=1;
	//myRange.font.ColorIndex=1;
	
	//mtext="某市政府红头文件";
	//DocObject.Application.Selection.Range.InsertAfter (mtext+"\n");
	//myRange=DocObject.Paragraphs(1).Range;
	//myRange.ParagraphFormat.LineSpacingRule =1.5;
	
	//myRange.Font.ColorIndex=6;
	//myRange.Font.Name="仿宋_GB2312";
	//myRange.font.Bold=true;
	//myRange.Font.Size=30;
	//myRange.ParagraphFormat.Alignment=1;
	alert("绿色版不支持VBA接口.")

}
//使用指定的模板套红
function ApplyFileTemplate() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
	{
		alert("绿色版没有此功能");
		//var mDialogUrl = "images/selectTemplate.htm";
		//var mObject = new Object();
		//mObject.SelectValue = "";
		//window.showModalDialog(mDialogUrl, mObject, "dialogHeight:180px; dialogWidth:340px;center:yes;scroll:no;status:no;"); 
		//判断用户是否选择签名
		//if (mObject.SelectValue!="")
		//{
		//	formData.SOAOfficeCtrl.ApplyTemplateFromURL(BaseUrl + "doc/" + mObject.SelectValue);
		//}
	}
} 
//禁止/允许WORD鼠标拖曳功能
function DisableDragAndDrop() 
{ 
	if (!bDocOpen)
		alert("当前没有已打开的文档。");
	else
		alert("绿色版不支持此功能。")
		//formData.SOAOfficeCtrl.Document.Application.Options.AllowDragAndDrop = !formData.SOAOfficeCtrl.Document.Application.Options.AllowDragAndDrop;
} 
//将html form的域值拷贝到Word文档的标签中
function CopyTextToBookMark(inputname,BookMarkName)
{
	try
	{	
		var inputValue="";
		var j,elObj,optionItem;
		var elObj = document.forms[0].elements(inputname);		 
		if (!elObj)
		{
			alert("HTML的FORM中没有此输入域："+ inputname);
			return;
		}
		switch(elObj.type)
		{
				case "select-one":
					inputValue = elObj.options[elObj.selectedIndex].text;
					break;
				case "select-multiple":
					var isFirst = true;
					for(j=0;j<elObj.options.length;j++)
					{
						optionItem = elObj.options[j];					
						if (optionItem.selected)
						{
							if(isFirst)
							{
								inputValue = optionItem.text;
								isFirst = false;
							}
							else
							{
								inputValue += "  " + optionItem.text;
							}
						}
					}
					
					break;
				default: // text,Areatext,selecte-one,password,submit,etc.
					inputValue = elObj.value;
					break;
		}
		var bkmkObj = formData.SOAOfficeCtrl.Document.BookMarks(BookMarkName);	
		if(!bkmkObj)
		{
			alert("Word 模板中不存在名称为：\""+BookMarkName+"\"的书签！");
		}
		var saverange = bkmkObj.Range
		saverange.Text = inputValue;
		formData.SOAOfficeCtrl.Document.Bookmarks.Add(BookMarkName,saverange);
	}
	catch(err){
		
	}
	finally{
	}		
}