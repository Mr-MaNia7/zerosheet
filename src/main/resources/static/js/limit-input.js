// var max = 50;
// var $inputs = $('input');

// function sumInputs($inputs) {
//   var sum = 0;
  
//   $inputs.each(function() {
//     sum += parseInt($(this).val(), 0);
//   });
//   return sum;
// }

// $inputs.on('input', function(e) {
//   var $this = $(this);
//   var sum = sumInputs($inputs.not(function(i, el) {
//     return el === e.target;
//   }));
//   var value = parseInt($this.val(), 10) || 0;
//   if(sum + value > max) $this.val(max - sum);
// });

$(":input").bind('keyup mouseup', function () {          
    var max = 10;
    var cat1 = $("#returnQuantity").val();
    var cat2 = $("#maintenanceQty").val();
    var cat3 = $("#defectedQty").val();
    var total = parseInt(cat1) + parseInt(cat2) + parseInt(cat3);
   
    if (total > max) {
        // alert(total);
        // var errorEl = document.getElementById("errorDiv");
        // errorEl.setAttribute = ("class", "invalid-feedback");
        // errorEl.textContent = "The sum exceeds";
    //   alert("it exceeds 50");
      return false;
    }
    
  });