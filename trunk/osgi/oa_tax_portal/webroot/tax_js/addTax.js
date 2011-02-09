function addTr()
{
	var list = initData();
    for (var i = 0; i < 2; i++)
    {
        addTrInner(list);
    }
}

function addTrInner(list)
{
    var table = $O("tables");
    
    var tr = $O("trCopy");
    
    trow =  table.insertRow(-1);
    
    if (length % 2 == 1)
    {
        trow.className = 'content2';
    }
    else
    {
        trow.className = 'content1';
    }
    
    for (var i = 0; i < tr.cells.length; i++)
    {
        var tcell = document.createElement("td");
        
        tcell.innerHTML = tr.cells[i].innerHTML;
        
        trow.appendChild(tcell);
    }
    
    trow.appendChild(tcell);
    
    new hint(getTrInnerObj2(trow, 'taxId'), list, HINT_MODE.CLIENT, false, null, callClick);
    
    return trow;
}

function removeTr(obj)
{
    //rows
    var table = $O("tables");
    
    if (table.rows.length <= 2)
    {
    	alert('不能全部删除');
    	
    	return false;
    }
    
    obj.parentNode.parentNode.removeNode(true);
    
    
    for (var i = 2; i < table.rows.length; i++)
    {
        if (i % 2 == 1)
        {
            table.rows[i].className = 'content1';
        }
        else
        {
            table.rows[i].className = 'content2';
        }
    }
}