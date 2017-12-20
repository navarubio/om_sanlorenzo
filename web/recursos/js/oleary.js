var imagePath = '';
var adulto = false;
var oleary1 = {idJsonField: '.jsonOleary1', idMasterContainer: '.oleary1', idBaseContainer: 'ole1', initTooth: 523, idTotal: '.totalPlaca1'};
var oleary2 = {idJsonField: '.jsonOleary2', idMasterContainer: '.oleary2', idBaseContainer: 'ole2', initTooth: 687, idTotal: '.totalPlaca2'};
function drawTableOleary(container, data) {
    $(container).append('<table style="margin:0 auto;"><tbody>\n\
<tr><td class="soloAdu">18</td><td class="soloAdu">17</td><td class="soloAdu">16</td><td>15-55</td><td>14-54</td><td>13-53</td><td>12-52</td><td>11-51</td><td>21-61</td><td>22-62</td><td>23-63</td><td>24-64</td><td>25-65</td><td class="soloAdu">26</td><td class="soloAdu">27</td><td class="soloAdu">28</td></tr>\n\
<tr><td class="soloAdu">48</td><td class="soloAdu">47</td><td class="soloAdu">46</td><td>45-85</td><td>44-84</td><td>43-83</td><td>42-82</td><td>41-81</td><td>31-71</td><td>32-72</td><td>33-73</td><td>34-74</td><td>35-75</td><td class="soloAdu">36</td><td class="soloAdu">37</td><td class="soloAdu">38</td></tr>\n\
<tbody></table>');
    $('.soloAdu').css('display', adulto ? '' : 'none');
    $.each($(container).find('table:last tbody tr td'), function (index, td) {
        var valor = $(td).text();
        $(td).text('');
        $(td).attr('id', data.idBaseContainer + index);
        drawToothOleary(data, $(td).get(0), valor);
    });
}
function getDataTeethOleary(data) {
    var json = [];
    var count = data.initTooth - 1;
    $.each($(data.idMasterContainer).find('table:last tr td'), function (index, td) {
        if ($(td).find('svg').length) {
            var idContainer = $(td).attr('id');
            json.push(String(++count) + '=olearyLimpio');
            $.each(['z1', 'z2', 'z3', 'z4'], function (index, item) {
                var selected = SVG.get(idContainer + '-' + item).data('selected');
                var img = 'oleary' + (selected ? 'Tenido' : 'Limpio');
                json.push(String(++count) + '=' + img);
            });
        }
    });
    $(data.idJsonField).val(JSON.stringify(json));
    return true;
}
function setDataTeethOleary(data, jsonData) {
    var json = JSON.parse(jsonData);
    var count = -1;
    $.each($(data.idMasterContainer).find('table:last tr td'), function (index, td) {
        if ($(td).find('svg').length) {
            var idContainer = $(td).attr('id');
            ++count;
            $.each(['z1', 'z2', 'z3', 'z4'], function (index, item) {
                var values = json[++count].split('=');
                var selected = /olearyTenido/.test(values[1]);
                SVG.get(idContainer + '-' + item)
                        .data({selected: selected})
                        .style({'fill': selected ? '#ff0000' : '#ffffff'});
            });
        }
    });
    return true;
}
function drawToothOleary(data, container, numTooth) {
    var idContainer = $(container).attr('id');
    var svg = '<svg xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:cc="http://creativecommons.org/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" width="60" height="60" viewBox="0 0 60.000001 60.000001" id="svg2" version="1.1" inkscape:version="0.91 r13725" sodipodi:docname="oleary.svg"> <defs id="defs4" /> <sodipodi:namedview id="base" pagecolor="#ffffff" bordercolor="#666666" borderopacity="1.0" inkscape:pageopacity="0.0" inkscape:pageshadow="2" inkscape:zoom="5.6" inkscape:cx="32.78516" inkscape:cy="35.796908" inkscape:document-units="px" inkscape:current-layer="layer1" showgrid="false" units="px" inkscape:window-width="1366" inkscape:window-height="705" inkscape:window-x="-8" inkscape:window-y="-8" inkscape:window-maximized="1" /> <metadata id="metadata7"> <rdf:RDF> <cc:Work rdf:about=""> <dc:format>image/svg+xml</dc:format> <dc:type rdf:resource="http://purl.org/dc/dcmitype/StillImage" /> <dc:title></dc:title> </cc:Work> </rdf:RDF> </metadata> <g inkscape:label="Capa 1" inkscape:groupmode="layer" id="layer1" transform="translate(0,-992.3622)"> <path style="opacity:1;fill:#ffffff;fill-opacity:1;stroke:#000000;stroke-width:0.55275589;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="z1" sodipodi:type="arc" sodipodi:cx="-744.40887" sodipodi:cy="-701.98383" sodipodi:rx="30" sodipodi:ry="30.000416" sodipodi:start="0" sodipodi:end="1.5655328" d="m -714.40887,-701.98383 a 30,30.000416 0 0 1 -29.8421,30 l -0.1579,-30 z" transform="matrix(-0.70710678,-0.70710678,0.70710678,-0.70710678,0,0)" inkscape:label="" /> <path style="opacity:1;fill:#ffffff;fill-opacity:1;stroke:#000000;stroke-width:0.55275589;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="z3" sodipodi:type="arc" sodipodi:cx="743.85608" sodipodi:cy="-701.98383" sodipodi:rx="30" sodipodi:ry="30.000416" sodipodi:start="0" sodipodi:end="1.5655328" d="m 773.85608,-701.98383 a 30,30.000416 0 0 1 -29.8421,30 l -0.1579,-30 z" transform="matrix(0.70710678,0.70710678,0.70710678,-0.70710678,0,0)" inkscape:label="" /> <path style="opacity:1;fill:#ffffff;fill-opacity:1;stroke:#000000;stroke-width:0.55275589;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="z2" sodipodi:type="arc" sodipodi:cx="-744.40887" sodipodi:cy="701.42822" sodipodi:rx="30" sodipodi:ry="30.000416" sodipodi:start="0" sodipodi:end="1.5655328" d="m -714.40887,701.42822 a 30,30.000416 0 0 1 -29.8421,30 l -0.1579,-30 z" transform="matrix(-0.70710678,-0.70710678,-0.70710678,0.70710678,0,0)" inkscape:label="" /> <path style="opacity:1;fill:#ffffff;fill-opacity:1;stroke:#000000;stroke-width:0.55275589;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="z4" sodipodi:type="arc" sodipodi:cx="743.85608" sodipodi:cy="701.42822" sodipodi:rx="30" sodipodi:ry="30.000416" sodipodi:start="0" sodipodi:end="1.5655328" d="m 773.85608,701.42822 a 30,30.000416 0 0 1 -29.8421,30 l -0.1579,-30 z" transform="matrix(0.70710678,0.70710678,-0.70710678,0.70710678,0,0)" inkscape:label="" /> </g> </svg>';
    var values = ['z1', 'z2', 'z3', 'z4'];
    $.each(values, function (index, item) {
        svg = svg.replace(item, idContainer + '-' + item);
    });
    var draw = SVG(container).size(60, 85);
    $(container).find('svg').append(svg);
    $.each(values, function (index, item) {
        SVG.get(idContainer + '-' + item)
                .style({'cursor': 'pointer'})
                .data({'selected': false})
                .click(function () {
                    onClickOleary(data, this);
                });
    });
    draw.svg(svg);
    var h = 62;
    $.each(numTooth.split('-'), function (index, item) {
        var text = draw.text(item);
        text.move(23, h)
                .style('font-size:10pt');
        h += 11;
    });
    draw.scale(0.8, 0.8);
    return draw;
}
function onClickOleary(data, element) {
    var selected = element.data('selected');
    element.style({'fill': selected ? '#ffffff' : '#ff0000'})
            .data({'selected': !selected});
    setTimeout(function () {
        calcDataOleary(data);
    }, 10);
}
function calcDataOleary(data) {
    var selecteds = 0;
    var allAdult = 32.0 * 4.0;
    var allChild = 20.0 * 4.0;
    var values = ['z1', 'z2', 'z3', 'z4'];
    $.each($(data.idMasterContainer + ' table tbody ').find(adulto ? 'td' : 'td[class!="soloAdu"]'), function (index, td) {
        var idContainer = $(td).attr('id');
        $.each(values, function (index, item) {
            selecteds += (SVG.get(idContainer + '-' + item).data('selected') ? 1 : 0);
        });
    });
    var total = selecteds / (adulto ? allAdult : allChild) * 100.0;
    var indOleary = '';
    if (total < 13.0) {
        indOleary = '%  (Aceptable)';
    } else if (total >= 13.0 && total < 24.0) {
        indOleary = '%  (Cuestionable)';
    } else if (total >= 24.0) {
        indOleary = '%  (Deficiente)';
    }
    $(data.idTotal).val((Math.round(total * 10) / 10) + indOleary);
}