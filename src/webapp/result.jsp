<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Point Blank</title>
    <link rel="stylesheet" href="style/main.css">
    <script src="script/init.js"></script>
    <script src="script/index.js" defer></script>
    <script src="script/graph.js" defer></script>
</head>
<body>
<header class="info">
    <div id="info">
        <p>Ryazanov Nikita</p>
        <p>P3207</p>
        <p>467307</p>
        <hr>
    </div>
</header>
<main class="container">
    <section class="graph-section">
        <canvas id="graph" width="480" height="480"></canvas>
    </section>
    <section class="result-section">
        <div>
            <table id="result-table">
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Time</th>
                    <th>Execution time</th>
                    <th>Result</th>
                </tr>
            </table>
        </div>
    </section>
</main>
<footer>
    <hr>
    <a href="https://github.com/s4dnex/web-lab2" target="_blank" id="github">Source Code</a>
</footer>
</body>
</html>