function drawBubble(ctx, x, y, w, h, radius) {
  let r = x + w;
  let b = y + h;
  ctx.beginPath();
  ctx.strokeStyle="black";
  ctx.lineWidth="2";
  ctx.moveTo(x+radius, y);
  ctx.lineTo(x+radius/2, y-10);
  ctx.lineTo(x+radius * 2, y);
  ctx.lineTo(r-radius, y);
  ctx.quadraticCurveTo(r, y, r, y+radius);
  ctx.lineTo(r, y+h-radius);
  ctx.quadraticCurveTo(r, b, r-radius, b);
  ctx.lineTo(x+radius, b);
  ctx.quadraticCurveTo(x, b, x, b-radius);
  ctx.lineTo(x, y+radius);
  ctx.quadraticCurveTo(x, y, x+radius, y);
  ctx.stroke();
}

let bubble = '<div class="boubleWrapper">' +
                '<canvas width="75" height="57" ></canvas>' +
                '<div class="bubbletext">Це місце зайняте</div>' +
             '</div>';

async function showMessage(elem) {
    if (elem.parentNode.getElementsByClassName('boubleWrapper').length === 0) {
        elem.parentNode.insertAdjacentHTML('beforeend', bubble);
        let canvas = elem.parentNode.getElementsByClassName('boubleWrapper')[0].getElementsByTagName('canvas')[0];
        drawBubble(canvas.getContext('2d'), 5, 15, 69, 40, 15);
        await new Promise(r => setTimeout(r, 1500)); //wait 1.5 sec

        canvas.parentNode.remove();
    }
}