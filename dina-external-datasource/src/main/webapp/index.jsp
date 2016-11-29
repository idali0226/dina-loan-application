<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>

    <head>
        <script type='text/javascript' src='https://www.google.com/jsapi'></script>
        <script type='text/javascript'>
            google.load('visualization', '1', {'packages': ['geomap']});
            google.setOnLoadCallback(drawVisualization);
  
            function drawVisualization() {   
                var query = new google.visualization.Query('externalData'); 
                query.send(handleQueryResponse);
            }
      
            function handleQueryResponse(response) {
                if (response.isError()) {
                    alert('Error in query: ' + response.getMessage() + ' ' + response.getDetailedMessage());
                    return;
                }
          
                var data = response.getDataTable();
          
          
                var options = {};
                options['region'] = 'SE';
                options['colors'] = [0xFF8747, 0xFFB581, 0xc06000]; //orange colors
                options['dataMode'] = 'Markers';
          
                var container = document.getElementById('map_canvas');
                var geomap = new google.visualization.GeoMap(container);
                geomap.draw(data, options); 
            }
        </script>
    </head>

    <body>
        <div id="map_canvas"></div>
    </body>

</html>
