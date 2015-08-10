
function load(json) {
	var myField = [];
	var myColumn = [];
	$.each(json, function(j, record){
		if (j == 0) {
			$.each(record, function(i, row){
				myField.push("col"+i);
				myColumn.push({text:row[0], dataIndex:"col"+i,width:200});
			});
		}
	});
	var myData = [];
	$.each(json, function(j, record){
		console.log(record);
		var temp = [];
		$.each(record, function(i, row){
			temp.push(row[1]);
		});
		myData.push(temp);
	});
	var userstore = Ext.create('Ext.data.Store',{
		storeId:'userstore',
        fields : myField,
        data   : myData
    });
	 $("#content").html("");
        Ext.widget({
            renderTo : "content",
            xtype    : 'grid',
            title    : '知识抽取结果',
            width    : 1200,
            height   : 600,
            plugins  : 'rowediting',
            store    : userstore,
            columns: myColumn
        })
 }