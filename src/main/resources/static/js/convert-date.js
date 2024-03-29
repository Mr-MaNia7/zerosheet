/**
 * @author መሃሪ <gmehari.edu@gmail.com>
 * @on APR 10 2017 , ነሃሴ 4 2009  ዓ/ም
 * Adopted from http://www.geez.org/Calendars/EthiopicCalendar.java
 */

const JD_EPOCH_OFFSET_AMETE_ALEM = -285019; //      ዓ/ዓ    
const JD_EPOCH_OFFSET_AMETE_MIHRET = 1723856; //    ዓ/ም
const JD_EPOCH_OFFSET_GREGORIAN = 1721426;
const JD_EPOCH_OFFSET_UNSET = -1;

let JDN_OFFSET = JD_EPOCH_OFFSET_UNSET;

const GREGORIAN_NUMBER_OF_MONTHS = 12;
const monthDays = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

/** HELPERS * */
function quotient(i, j) {
  return Math.floor(i / j);
}

function mod(i, j) {
  return i % j;
}

function isGregorianLeap(year) {
  return (year % 4 === 0) && ((year % 100 !== 0) || (year % 400 === 0));
}

/** ERA HELPERS */
function setEra(era) {
  if ((era === JD_EPOCH_OFFSET_AMETE_ALEM) || (era === JD_EPOCH_OFFSET_AMETE_MIHRET)) {
    JDN_OFFSET = era;
  } else {
    throw (new Error("Unknown Era:", era));
  }
}

function isEraSet() {
  return JD_EPOCH_OFFSET_UNSET !== JDN_OFFSET;
}

function unsetEra() {
  JDN_OFFSET = JD_EPOCH_OFFSET_UNSET;
}

function guessEraFromJDN(jdn) {
  return (jdn >= (JD_EPOCH_OFFSET_AMETE_MIHRET + 365)) ?
    JD_EPOCH_OFFSET_AMETE_MIHRET : JD_EPOCH_OFFSET_AMETE_ALEM;
}

/** CONVERSION * */
function ethiopicToJDN(day, month, year) {
  const ERA = isEraSet() ? JDN_OFFSET : JD_EPOCH_OFFSET_AMETE_MIHRET;
  const jdn = (ERA + 365) +
    365 * (year - 1) +
    quotient(year, 4) +
    30 * month +
    day - 31;

  return jdn;
}

function jdnToEthiopic(jdn) {
  const ERA = isEraSet() ? JDN_OFFSET : guessEraFromJDN(jdn);
  const r = mod((jdn - ERA), 1461);
  const n = mod(r, 365) + 365 * quotient(r, 1460);
  const year = 4 * quotient((jdn - ERA), 1461) +
    quotient(r, 365) -
    quotient(r, 1460);
  const month = quotient(n, 30) + 1;
  const day = mod(n, 30) + 1;

  return [day, month, year];
}

function gregorianToJDN(day, month, year) {
  const s = quotient(year, 4) -
    quotient(year - 1, 4) -
    quotient(year, 100) +
    quotient(year - 1, 100) +
    quotient(year, 400) -
    quotient(year - 1, 400);

  const t = quotient(14 - month, 12);

  const n = 31 * t * (month - 1) +
    (1 - t) * (59 + s + 30 * (month - 3) + quotient((3 * month - 7), 5)) +
    day - 1;

  const j = JD_EPOCH_OFFSET_GREGORIAN +
    365 * (year - 1) +
    quotient(year - 1, 4) -
    quotient(year - 1, 100) +
    quotient(year - 1, 400) +
    n;

  return j;
}

function jdnToGregorian(jdn) {
  const r2000 = mod((jdn - JD_EPOCH_OFFSET_GREGORIAN), 730485);
  const r400 = mod((jdn - JD_EPOCH_OFFSET_GREGORIAN), 146097);
  const r100 = mod(r400, 36524);
  const r4 = mod(r100, 1461);
  let n = mod(r4, 365) + 365 * quotient(r4, 1460);
  const s = quotient(r4, 1095);
  const aprime = 400 * quotient((jdn - JD_EPOCH_OFFSET_GREGORIAN), 146097) +
    100 * quotient(r400, 36524) +
    4 * quotient(r100, 1461) +
    quotient(r4, 365) -
    quotient(r4, 1460) -
    quotient(r2000, 730484);
  const year = aprime + 1;
  const t = quotient((364 + s - n), 306);
  let month = t * (quotient(n, 31) + 1) + (1 - t) * (quotient((5 * (n - s) + 13), 153) + 1);
  n += 1 - quotient(r2000, 730484);
  let day = n;


  if ((r100 === 0) && (n === 0) && (r400 !== 0)) {
    month = 12;
    day = 31;
  } else {
    monthDays[2] = (isGregorianLeap(year)) ? 29 : 28;
    for (let i = 1; i <= GREGORIAN_NUMBER_OF_MONTHS; i += 1) {
      if (n <= monthDays[i]) {
        day = n;
        break;
      }
      n -= monthDays[i];
    }
  }
  return [year, month, day];

}

function gregorianToEthiopic(day, month, year) {
  const jdn = gregorianToJDN(day, month, year);
  return jdnToEthiopic(jdn);
}

function ethioipicToGreg(day, month, year) {
  const jdn = ethiopicToJDN(day, month, year);
  return jdnToGregorian(jdn);
}

function ethioipicToGregorian(day, month, year, era) {
  setEra(era);
  const result = ethioipicToGreg(day, month, year);
  unsetEra();
  return result;
}

/** API * */

/** ethiopian to gregorian */
function toGC(dateArray) {
  const [y, m, d] = dateArray
  let era = dateArray[3];
  if (d < 0 || d > 30 || m < 0 || m > 13) {
    throw new Error('Invalid Ethiopian Date');
  }
  if (!era) {
    era = JD_EPOCH_OFFSET_AMETE_MIHRET;
  }
  return ethioipicToGregorian(d, m, y, era);
}

/** gregorian to ethiopian */
function toEC(dateArray) {
  const [d, m, y] = dateArray;
  if (d < 0 || d > 31 || m < 0 || m > 12) {
    throw new Error('Invalid Gregorian Date');
  }
  return gregorianToEthiopic(d, m, y);
}

/** format ethiopian date with amharic names */
function formatDate(dateArray) {
  var months = ["መስከረም", "ጥቅምት", "ህዳር", "ታህሳስ", "ጥር", "የካቲት", "መጋቢት", "ሚያዚያ", "ግንቦት", "ሰኔ", "ሐምሌ", "ነሐሴ", "ጳጉሜ"];
  const [d, m, y] = dateArray;
  return `${months[m-1]} ${d} ቀን ${y} ዓ.ም`;
}

Array.from(document.querySelectorAll('.date')).forEach(function (dateEl) {
  var dateArray = dateEl.textContent.split('-')
  var dateNumArray = dateArray.map(str => { return Number(str) })
  var etDate = toEC(dateNumArray)
  dateEl.textContent = formatDate(etDate)
  Array.from(document.querySelectorAll('.year')).forEach(function (agreementNum) {
    agreementNum.textContent = `/${etDate[2]%1000}`
  })
})

Array.from(document.querySelectorAll('.datewclock')).forEach(function (dateEl) {
  var dateArray = dateEl.textContent.split('-')
  var day = dateArray[2].split(" ")[0]
  var clock = dateArray[2].split(" ")[1].split(":")

  var hour = Number(clock[0]) % 12
  if (Number(clock[0]) >= 12) {
    var suffix = 'PM'
  }
  else {
    var suffix = 'AM'
  }
  var min = Number(clock[1])
  var sec = Number(clock[2].split(".")[0])
  var millisec = Number(clock[2].split(".")[1])

  var etDate = toEC([Number(day), Number(dateArray[1]), Number(dateArray[0])])
  dateEl.textContent = `${formatDate(etDate)} ${hour}:${min} ${suffix} EAT`
})


function defaultDate() {
  var dueDate = document.getElementById("dueDate")
  var dueDateArray = dueDate.getAttribute("value").split("-")
  var dueDateNumArray = dueDateArray.map(str => { return Number(str) })
  var dueDateET = toEC([dueDateNumArray[2], dueDateNumArray[1], dueDateNumArray[0]])
  var dueDateEC = document.getElementById("dueDateEC")
  dueDateEC.textContent = `= ${formatDate(dueDateET)}`

  var dueBackDate = document.getElementById("dueBackDate")
  var dueBackDateArray = dueBackDate.getAttribute("value").split("-")
  var dueBackDateNumArray = dueBackDateArray.map(str => { return Number(str) })
  var dueBackDateET = toEC([dueBackDateNumArray[2], dueBackDateNumArray[1], dueBackDateNumArray[0]])
  var dueBackDateEC = document.getElementById("dueBackDateEC")
  dueBackDateEC.textContent = `= ${formatDate(dueBackDateET)}`
}

var due_date = document.getElementById("dueDate")
due_date.addEventListener('change', function(){
  var dateArray = this.value.split("-")
  var dateNumArray = dateArray.map(str => { return Number(str) })
  var etDate = toEC([dateNumArray[2], dateNumArray[1], dateNumArray[0]])
  var dueDateEC = document.getElementById("dueDateEC")
  dueDateEC.textContent = `= ${formatDate(etDate)}`
})

var due_back_date = document.getElementById("dueBackDate")
due_back_date.addEventListener('change', function(){
  var dateArray = this.value.split("-")
  var dateNumArray = dateArray.map(str => { return Number(str) })
  var etDate = toEC([dateNumArray[2], dateNumArray[1], dateNumArray[0]])
  var dueDateEC = document.getElementById("dueBackDateEC")
  dueDateEC.textContent = `= ${formatDate(etDate)}`
})

var area = document.getElementById("areaCoverage")
area.addEventListener('change', function(){
  var a = this.value
  var qtyel = document.getElementById("itemQuantity")
  qtyel.value = `${Math.round(a / 3.84)}`
})

var itemQty = document.getElementById("itemQuantity")
itemQty.addEventListener('change', function(){
  var q = this.value
  var areael = document.getElementById("areaCoverage")
  areael.value = `${Math.round(q * 3.84)}`
})

function defaultValue() {
  var selectedElement = document.getElementById('itemSel')
  var loanprice = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-loanprice')
  var totalquantity = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-totalquantity')
  var area = document.getElementById("areaCoverage").getAttribute('value')
  var itemId = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-itemId')
  var comboEl = document.getElementById("combo")

  var priceElement = document.getElementById('itemPrice')
  priceElement.setAttribute('value', `${loanprice}`)

  var quantityElement = document.getElementById('itemQuantity')
  quantityElement.setAttribute('max', `${totalquantity}`)
  quantityElement.setAttribute('value', `${Math.round(area/3.84)}`)

  if (itemId == 1 || itemId == 2) {
    comboEl.setAttribute("class", "collapse show")
  }

  defaultDate()
  
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
  const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
  
}

function changeHandler() {
  var selectedElement = document.getElementById('itemSel')
  var loanprice = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-loanprice')
  var totalquantity = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-totalquantity')
  var itemId = selectedElement.options[selectedElement.selectedIndex].getAttribute('data-bs-itemId')
  var comboEl = document.getElementById("combo")

  var priceElement = document.getElementById('itemPrice')
  priceElement.setAttribute('value', `${loanprice}`)

  if (itemId == 1 || itemId == 2) {
    comboEl.setAttribute("class", "collapse show")
  }
  else {
    comboEl.setAttribute("class", "collapse")
  }

  var quantityElement = document.getElementById('itemQuantity')
  quantityElement.setAttribute('max', `${totalquantity}`)
}
