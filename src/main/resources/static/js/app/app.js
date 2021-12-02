$('#assetListModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var productID = $(button).attr("data-product-id");
    var selectedUnit = $('#from_' + productID + ' option:selected').val();
    var roID = $('#inputRoId').val()
    $.get("/item/shipment/" + roID + "?product_id=" + productID + "&unit_id=" + selectedUnit, function (data) {
            var content = $($.parseHTML(data)).find(".modal-body").html();
            $('#assetListModal').find('.modal-body').html(content);
        }
    )
});


function removeAsset(caller, event) {
    event.preventDefault();
    var action = $(caller).attr("data-action");
    console.log(action)
    $.post(action, function (data) {
        location.reload();
    });
}