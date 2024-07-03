
<!doctype html>
<html>

<head>
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover" />
    <meta name="format-detection" content="telephone=no" />
    <style>

        /* @font-face {
          font-family: 'HarmonyOSHans-Regular';
          src: url('./HarmonyOS_Sans_SC_Regular.ttf');
        } */
        * { margin: 0; padding: 0; }

        html,body{margin: 0; padding: 0; font-family: HarmonyOSHans-Regular,Arial,Helvetica,sans-serif !important; overflow-x: hidden; }
        img{ max-width: 100%; height: auto; }
        video{ width: 66%; margin: 0 auto; display: block; }
        .h1{ font-size: 56px; line-height: 74px; font-weight: bolder; }
        .h2{ font-size: 40px; line-height: 60px; font-weight: bolder; }
        .h3{ font-size: 36px; line-height: 54px; font-weight: bold; }


        body {padding: calc(20px + env(safe-area-inset-top)) calc(20px + env(safe-area-inset-right)) 0 calc(20px + env(safe-area-inset-left));}
        .richtext-wrap{ margin: 0 auto; }
        .richtext-wrap p {  margin: 0 0 15px 0; }

        table { border: 1px solid #ccc; border-collapse: collapse; }
        table th { border: 1px solid #ccc; background-color: #f5f2f0;}
        table td { border: 1px solid #ccc; }

        /* 暗黑模式css，后期可补充一些特殊的样式的覆盖 */
        .dark-mode { background-color: #0f1013; color: #d9d9d9; }
        .dark-mode img, .dark-mode video { opacity: 0.8; }
        .dark-mode table th { border: 1px solid #ccc; background-color: #555555;}

    </style>
</head>

<body>
<div class="richtext-wrap">
    ${article}

</div>
<script>
    var userAgent = navigator.userAgent

    function detectThemeType () {
        var regex = /ThemeType\/(\S*)/g
        var match = regex.exec(userAgent)
        console.log(match)
        if (match) {
            document.body.className = match[1] + '-mode'
        }
    }
    detectThemeType()
</script>
</body>

</html>