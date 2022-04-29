window.addEventListener('DOMContentLoaded', event => {
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatablesSimple = document.getElementById('datatablesSimple');
    // if (datatablesSimple) {
        const table = new simpleDatatables.DataTable("#datatablesSimple");
    // }

    let columns = table.columns();
    let columnsLength = columns.dt.columnWidths.length;
    let skipCol = [columnsLength-3, columnsLength-2, columnsLength-1];

    $(".csv").click(function (){
        table.export({
            type:'csv',
            filename:'download csv',
            download:true,
            skipColumn: skipCol,
            lineDelimiter:  "\n",
            columnDelimiter:  ",",
        })
    })

    $(".json").click(function (){
        table.export({
            type:'json',
            filename:'download json',
            download:true,
            skipColumn: skipCol,
            escapeHTML: true,
            space: 3
        })
    })

    $(".sql").click(function (){
        table.export({
            type:"sql",
            download: true,
            filename:'download sql',
            tableName: "export_table",
            skipColumn: skipCol
        })
    })



});
