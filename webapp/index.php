<html>
    <body>


        <?php
        $Page = filter_input(INPUT_GET, 'p');
        $Data = filter_input(INPUT_POST, 'data');
        if ($Data > '') {
            $Page = 'data';
        }

        switch ($Page) {
            case "short" :
                ?>

                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.vibrate('SHORT');<br />
                }
                <br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.vibrate('SHORT');
                    }
                </script>
                <h2><a href="index.php">back</a></h2>

                <?php
                break;
            case "med" :
                ?>

                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.vibrate('MED');<br />
                }
                <br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.vibrate('MED');
                    }
                </script>
                <h2><a href="index.php">back</a></h2>

                <?php
                break;
            case "long" :
                ?>

                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.vibrate('LONG');<br />
                }
                <br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.vibrate('LONG');
                    }
                </script>
                <h2><a href="index.php">back</a></h2>

                <?php
                break;
            case "pattern" :
                ?>

                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.vibrate('PATTERN{0, 100, 300, 100, 300, 100, 300}');<br />
                }
                <br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.vibrate('PATTERN{0, 100, 300, 100, 300, 100, 300}');
                    }
                </script>
                <h2><a href="index.php">back</a></h2>

                <?php
                break;
            case "sign" :
                ?>

                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.getSignature();<br />
                }<br />
                function setSignature(imgdata) {<br />
                document.getElementById("imgdata").innerHTML = imgdata;<br />
                }<br />
                <br />
                function setSignatureImage(imgimage) {<br />
                document.getElementById("imgimage").innerHTML = imgimage;<br />
                }<br />
                &lt;/script&gt;<br />

                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.getSignature();
                    }

                    function setSignature(imgdata) {
                        document.getElementById("imgdata").innerHTML = imgdata;
                    }

                    function setSignatureImage(imgimage) {
                        document.getElementById("imgimage").innerHTML = imgimage;
                    }
                </script>
                <h2><a href="index.php">back</a></h2>
                Image: <p id="imgimage">[image]</p>
                Base64: <p id="imgdata">[base64 data]</p>
                <?php
                break;
            case "location" :
                ?>
               <div>Location: <p id="data">[data]</p></div><br />                
                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.getLocation();<br />
                }
                function setLocation(p1) {<br />
                alert(p1);<br />
                }<br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.getLocation();
                    }

                    function setLocation(data) {
                        document.getElementById("data").innerHTML = data;
                    }
                </script>
                <h2><a href="index.php">back</a></h2>
                <?php
                break;
            case "barcode" :
                ?>
                <div>Barcode: <p id="data">[data]</p></div><br />                
                &lt;script type='text/javascript'&gt;<br />
                if (typeof WebAppRunner !== 'undefined') {<br />
                WebAppRunner.scanBarcode();<br />
                }
                function setBarcode(p1) {<br />
                alert(p1);<br />
                }<br />
                &lt;/script&gt;<br />
                <script type='text/javascript'>
                    if (typeof WebAppRunner !== 'undefined') {
                        WebAppRunner.scanBarcode();
                    }

                    function setBarcode(data) {
                        document.getElementById("data").innerHTML = data;
                    }
                </script>
                <h2><a href="index.php">back</a></h2>
                <?php
                break;
            default:
                ?>
                <h1>WebApp Runner</h1>
                <p>Try the demo :</p>
                <p><a href="index.php?p=short">Vibrate SHORT</a></p>
                <p><a href="index.php?p=med">Vibrate MED</a></p>
                <p><a href="index.php?p=long">Vibrate LONG</a></p>
                <p><a href="index.php?p=pattern">Vibrate PATTERN</a></p>
                <p><a href="index.php?p=location">Get location</a></p>
                <p><a href="index.php?p=sign">Get signature</a></p>
                <p><a href="index.php?p=barcode">Get Barcode</a></p>
                <p></p>
                <p>This is beta version. Planned completion 12-20-2015. After release, the program price is $10 / year / device</p>
                <p>Download application (zip)</p>
                <p><a href="WebAppRunner.1.0.3.zip">WebAppRunner 1.0.3</a></p>
                <p><a href="WebAppRunner.1.0.2.zip">WebAppRunner 1.0.2</a></p>
                <p><a href="WebAppRunner.1.0.1.zip">WebAppRunner 1.0.1</a></p>
                <p><a href="WebAppRunner.1.0.0.zip">WebAppRunner 1.0.0</a></p>
                <p></p>
                <p>Please, pay a beer ;-)</p>
                <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
                  <input type="hidden" name="cmd" value="_s-xclick">
                  <input type="hidden" name="hosted_button_id" value="GYKRTP6NXWXQS">
                  <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
                  <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
                </form>
                <?php
                break;
        }
        ?>
    </body>
</html>