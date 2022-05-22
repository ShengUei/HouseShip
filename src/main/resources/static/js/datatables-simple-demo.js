window.addEventListener('DOMContentLoaded', event => {
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatablesSimple = document.getElementById('datatablesSimple');
    const table = new simpleDatatables.DataTable("#datatablesSimple", {
        labels: {
            placeholder: "查詢", // The search input placeholder
            perPage: "{select} 每頁顯示筆數", // per-page dropdown label
            noRows: "查無資料", // Message shown when there are no records to show
            noResults: "查無匹配資料", // Message shown when there are no search results
            info: "顯示第 {start} 到 {end} 筆資料，共 {rows} 筆" //
        }
    });

    let columns = table.columns();
    let columnsLength = columns.dt.columnWidths.length;
    // let skipCol = [columnsLength-3, columnsLength-2, columnsLength-1];
    let skipCol = [columnsLength - 1];

    $(".csv").click(function () {
        table.export({
            type: 'csv',
            filename: 'download csv',
            download: true,
            skipColumn: skipCol,
            lineDelimiter: "\n",
            columnDelimiter: ",",
        })
    })

    $(".json").click(function () {
        table.export({
            type: 'json',
            filename: 'download json',
            download: true,
            skipColumn: skipCol,
            escapeHTML: true,
            space: 3
        })
    })

    $(".sql").click(function () {
        table.export({
            type: "sql",
            download: true,
            filename: 'download sql',
            tableName: "export_table",
            skipColumn: skipCol
        })
    })


});
