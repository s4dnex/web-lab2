const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

const width = canvas.width;
const height = canvas.height;
const R = width / 2.5;
const centerX = width / 2;
const centerY = height / 2;

function drawPoint(x, y, r, isInside) {
    const scale = R / r;
    const radius = R / 100;
    const px = centerX + x * scale;
    const py = centerY - y * scale;

    ctx.beginPath();
    ctx.arc(px, py, radius, 0, 2 * Math.PI);
    ctx.fillStyle = isInside ? "lime" : "red";
    ctx.fill();
}

// Axis
{
    const arrowLength = 6;
    const divLineLength = 6;
    const gap = 6;
    ctx.font = "bold 1rem monospace";
    ctx.fillStyle = "rgba(255, 255, 255, 1)";
    
    // X-axis
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(width, centerY);
    ctx.fillText("X", width - 8, centerY + 20);
    // Y-axis
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, height);
    ctx.fillText("Y", centerX + 10, 10);
    ctx.strokeStyle = "white";
    ctx.stroke()
    
    // X-axis arrow
    ctx.beginPath();
    ctx.lineTo(width - arrowLength, centerY + arrowLength);
    ctx.lineTo(width - arrowLength, centerY - arrowLength);
    ctx.lineTo(width, centerY);
    ctx.fill();
    // Y-axis arrow
    ctx.beginPath();
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX + arrowLength, arrowLength);
    ctx.lineTo(centerX - arrowLength, arrowLength);
    ctx.fill();

    ctx.moveTo(centerX - R / 2, centerY + divLineLength / 2);
    ctx.lineTo(centerX - R / 2, centerY - divLineLength / 2);
    ctx.moveTo(centerX - R, centerY + divLineLength / 2);
    ctx.lineTo(centerX - R, centerY - divLineLength / 2);
    ctx.moveTo(centerX + R / 2, centerY + divLineLength / 2);
    ctx.lineTo(centerX + R / 2, centerY - divLineLength / 2);
    ctx.moveTo(centerX + R, centerY + divLineLength / 2);
    ctx.lineTo(centerX + R, centerY - divLineLength / 2);

    ctx.moveTo(centerX - divLineLength / 2, centerY - R);
    ctx.lineTo(centerX + divLineLength / 2, centerY - R);
    ctx.moveTo(centerX - divLineLength / 2, centerY - R / 2);
    ctx.lineTo(centerX + divLineLength / 2, centerY - R / 2);
    ctx.moveTo(centerX - divLineLength / 2, centerY + R / 2);
    ctx.lineTo(centerX + divLineLength / 2, centerY + R / 2);
    ctx.moveTo(centerX - divLineLength / 2, centerY + R);
    ctx.lineTo(centerX + divLineLength / 2, centerY + R);
    ctx.stroke();

    ctx.fillText("-R/2", centerX - R / 2 - gap, centerY - gap);
    ctx.fillText("-R", centerX - R - gap, centerY - gap);
    ctx.fillText("0", centerX + gap, centerY - gap);
    ctx.fillText("R/2", centerX + R / 2 - gap, centerY - gap);
    ctx.fillText("R", centerX + R - gap, centerY - gap);


    ctx.fillText("R/2", centerX + gap, centerY - R / 2 + gap);
    ctx.fillText("R", centerX + gap, centerY - R + gap);
    ctx.fillText("-R/2", centerX + gap, centerY + R / 2 + gap);
    ctx.fillText("-R", centerX + gap, centerY + R + gap);
}

// Areas
{
    ctx.fillStyle = "rgba(250, 250, 250, 0.2)";

    // I
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, R / 2, 0, -Math.PI / 2, true);
    ctx.lineTo(centerX, centerY);
    ctx.fill();

    // II

    // III
    ctx.beginPath();
    ctx.rect(centerX, centerY, -R, R);
    ctx.fill();

    // IV
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX + R, centerY);
    ctx.lineTo(centerX, centerY + R);
    ctx.closePath();
    ctx.fill();
}
