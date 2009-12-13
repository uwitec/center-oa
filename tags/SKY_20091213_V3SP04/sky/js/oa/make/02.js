function addBean()
{
    $O('method').value = 'passMake';
    
    if ($$('handerId') == '' || $$('handerId') == null)
    {
        alert('请选择下一环处理人');
        
        return false;
    }
    
    submit('确定通过申请?');
}

function rejectMake()
{
    $O('method').value = 'rejectMake';
    
    submit('确定驳回申请?');
}

function selectNext()
{
    if (role == '1')
    {
        window.common.modal('../make/make.do?method=rptQueryCreate&selectMode=1&id=' + makeid);
    }
    else
    {
        window.common.modal('../admin/pop.do?method=rptQueryGroupMember&selectMode=1&pid=' + role);
    }
}

function getStaffers(oo)
{
    if (oo.length > 0)
    {
        var item = oo[0];
        $O("handerName").value = item.pname;
        $O("handerId").value = item.value;
    }
}

function getMakeTokens(oo)
{
	if ($$('tokenAjax') == null)
	{
		alert('请选择环节');
		return;
	}
	
    $O("rejectTokenId").value = $$('tokenAjax');
    
    $O('method').value = 'rejectTokenMake';
    
    $('#dlg1').dialog({closed:true});
    
    submit();
}

function queryLog()
{
    window.common.modal('../admin/pop.do?method=rptQueryLog&fk=' + makeid);
}

function load()
{
    setAllReadOnly3(null, ['reason', 'handerId']);
    
    $('#dlg1').dialog({
                //iconCls: 'icon-save',
                modal:true,
                closed:true,
                buttons:{
                    '确 定':function(){
                        getMakeTokens();
                    },
                    '取 消':function(){
                        $('#dlg1').dialog({closed:true});
                    }
                }
     });
}

function editTemplate(docURL) 
{
    if (window.ActiveXObject)
    {
        var openDocObj = new ActiveXObject("SharePoint.OpenDocuments.2");
    } 
    else
    {
        alert('请使用IE进行操作');
        return;
    }
    
    openDocObj.editDocument(baseURL + docURL); 
    
}

function viewTemplate(docURL) 
{
    if (window.ActiveXObject)
    {
        var openDocObj = new ActiveXObject("SharePoint.OpenDocuments.2");
    } 
    else
    {
        alert('请使用IE进行操作');
        return;
    }
    
    openDocObj.ViewDocument(baseURL + docURL); 
}

function rejectTokenMake()
{
	if (formCheck())
	{
		$ajax('../make/make.do?method=queryHistoryToken&id=' + makeid, callBackFunRejectToken);
	}
}

function callBackFunRejectToken(data)
{
	var tokens = data.msg;
	
	var htm = '';
	
    for(var i = 0; i < tokens.length; i++)
    {
        var item = tokens[i];
        
        var llog = '<input type=radio name=tokenAjax value=' + item.id + '>' + item.name + '<br>';
        
        htm += llog;
    }
    
    $O('dialog_inner').innerHTML = htm;
	
	$('#dlg1').dialog({closed:false});
}


