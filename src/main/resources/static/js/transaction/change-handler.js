function defaultValue() {
    var selectedElement = document.getElementById('itemSel')
    var loanprice = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-loanprice')
    var totalquantity = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-totalquantity')

    var priceElement = document.getElementById('itemPrice')
    priceElement.setAttribute('value', `${loanprice}`)

    var quantityElement = document.getElementById('itemQuantity')
    quantityElement.setAttribute('max', `${totalquantity}`)

    
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
    
}

function changeHandler() {
    var selectedElement = document.getElementById('itemSel')
    var loanprice = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-loanprice')
    var totalquantity = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-totalquantity')

    var priceElement = document.getElementById('itemPrice')
    priceElement.setAttribute('value', `${loanprice}`)

    var quantityElement = document.getElementById('itemQuantity')
    quantityElement.setAttribute('max', `${totalquantity}`)
}