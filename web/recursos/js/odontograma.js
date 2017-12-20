var imagePath = '';
var adulto = false;
var odonData = {selected: 'clean', idJsonField: '.jsonOdontograma', idMasterContainer: '.odontograma', idBaseContainer: 'odo', idCariados: '.odoCariados', idPerdidos: '.odoPerdidos', idObturados: '.odoObturados', idSanos: '.odoSanos', values: {}};
var evolData = {selected: 'clean', idJsonField: '.jsonEvolucion', idMasterContainer: '.evolucion', idBaseContainer: 'evo', values: {}};
function setDataGraphics() {
    getDataTeeth(odonData);
    getDataTeeth(evolData);
    getDataTeethOleary(oleary1);
    getDataTeethOleary(oleary2);
}
function drawTable(container, data) {
    $(container).append('<table style="width:70%;margin:0 auto;"><tbody></tbody></table>');
    $.each(Object.keys(data.values), function (index, key) {
        if (index % 10 === 0) {
            $(container).find('table tbody').append('<tr></tr>');
        }
        $(container).find('table tbody tr:last').append('<td style="text-align:center"><div id="' + data.idBaseContainer + String(index) + '"></div><br><div class="ui-button ui-widget ui-corner-all"></div></td>');
        if (/LIMPIAR|EXODONCIA_INDICADA|DIENTE_EXTRAIDO|DIENTE_SIN_ERUPCIONAR|PROTESIS_FIJA_PONTICO|DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL|PROVISIONAL|DIENTE_REMP_PROTESIS_REMOVIBLE|DIENTE_EN_ERUPCION|DIENTE_PARCIALMENTE_ERUPCIONADO|SELLANTE_EN_BOCA|IMPLANTE/g.test(key)) {
            var draw = drawBaseTooth($(container).find('table tbody tr:last td:last div:first').get(0), 80);
            draw.scale(0.9, 0.9);
            data.selected = key;
            onClickBase($(container).find('table tbody tr:last td:last div:first').attr('id'), key, data);
            if (key === 'LIMPIAR') {
                cleanTooth(data.idBaseContainer + String(index));
            } else {
                onClickTooth(SVG.get(data.idBaseContainer + String(index) + '-' + key), data);
            }
        } else {
            $(container).find('table tbody tr:last td:last div:first')
                    .css({'cursor': 'pointer', 'height': '80px', 'width': '90px', 'border': 'solid 3px', 'background-color': data.values[key].color})
                    .click(function () {
                        data.selected = key;
                    });
        }
        $(container).find('table tbody tr:last td:last div:first')
                .css({'margin': 'auto'});
        $(container).find('table tbody tr:last td:last div:last')
                .text(key.replace(/_/g, ' '))
                .css({'cursor': 'pointer', 'height': '35px', 'font-size': '9px'})
                .click(function () {
                    data.selected = $(this).text().replace(/ /g, '_');
                });
    });
    $(container).append('<table class="teeth" style="margin:0 auto"><tbody>\n\
<tr class="adu"><td>18</td><td>17</td><td>16</td><td>15</td><td>14</td><td>13</td><td>12</td><td>11</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td></tr>\n\
<tr class="nin"><td>58</td><td>57</td><td>56</td><td>55</td><td>54</td><td>53</td><td>52</td><td>51</td><td>61</td><td>62</td><td>63</td><td>64</td><td>65</td><td>66</td><td>67</td><td>68</td></tr>\n\
<tr class="nin"><td>88</td><td>87</td><td>86</td><td>85</td><td>84</td><td>83</td><td>82</td><td>81</td><td>71</td><td>72</td><td>73</td><td>74</td><td>75</td><td>76</td><td>77</td><td>78</td></tr>\n\
<tr class="adu"><td>48</td><td>47</td><td>46</td><td>45</td><td>44</td><td>43</td><td>42</td><td>41</td><td>31</td><td>32</td><td>33</td><td>34</td><td>35</td><td>36</td><td>37</td><td>38</td></tr>\n\
<tbody></table>');
    $('.adu').css('display', adulto ? '' : 'none');
    $('.nin').css('display', !adulto ? '' : 'none');
    var dienteAdulto = null;
    var dienteNino = null;
    $.each($(container).find('table:last tbody tr'), function (index, tr) {
        $.each($(tr).find('td'), function (i, td) {
            var valor = $(td).text();
            $(td).text('');
            $(td).attr('id', data.idBaseContainer + (index + 1) + '_' + (i + 1) + '_');
            if (!(/^1|2$/.test(String(index)) && /^(0|1|2|13|14|15)_$/.test(String(i) + '_'))) {
                if (/^0|3$/.test(String(index))) {
                    dienteAdulto = valor === '18' ? 90 : dienteAdulto += 8;
                } else {
                    dienteNino = valor === '58' ? 346 : dienteAdulto += 8;
                }
                drawTooth($(td).get(0), valor, /^0|3$/.test(String(index)) ? dienteAdulto : dienteNino, data);
                cleanTooth($(td).attr('id'));
            }
        });
    });
}
function onClickBase(idContainer, selected, data) {
    var values = ['EXODONCIA_INDICADA', 'DIENTE_EXTRAIDO', 'DIENTE_SIN_ERUPCIONAR', 'PROTESIS_FIJA_PONTICO', 'DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL', 'PROVISIONAL', 'DIENTE_REMP_PROTESIS_REMOVIBLE', 'DIENTE_EN_ERUPCION', 'DIENTE_PARCIALMENTE_ERUPCIONADO', 'SELLANTE_EN_BOCA', 'IMPLANTE'];
    $.each(values, function (index, value) {
        SVG.get(idContainer + '-' + value)
                .style({'cursor': 'pointer'})
                .on('click', function () {
                    data.selected = selected;
                });
    });
    for (i = 1; i < 8; i++) {
        SVG.get(idContainer + '-z' + i)
                .style({'cursor': 'pointer'})
                .on('click', function () {
                    data.selected = selected;
                });
    }
}
function getDataTeeth(data) {
    var json = [];
    $.each($(data.idMasterContainer).find('table:last tr td'), function (index, td) {
        if ($(td).find('svg').length) {
            var idContainer = $(td).attr('id');
            var tooth = SVG.get(idContainer + '-d').data('dataTooth');
            json.push(String(tooth) + '=' + data.values[SVG.get(idContainer + '-d').data('selected')].value);
            for (i = 1; i < 8; i++) {
                json.push(String(tooth + i) + '=' + data.values[SVG.get(idContainer + '-z' + i).data('selected')].value);
            }
        }
    });
    $(data.idJsonField).val(JSON.stringify(json));
    return true;
}
function setDataTeeth(data, jsonData) {
    var json = JSON.parse(jsonData);
    var count = -1;
    $.each($(data.idMasterContainer).find('table:last tr td'), function (index, td) {
        if ($(td).find('svg').length) {
            var idContainer = $(td).attr('id');
            var values = json[++count].split('=');
            var value = searchValue(data, values[1]);
            SVG.get(idContainer + '-d').data({'dataTooth': Number(values[0]), selected: value});
            data.selected = value;
            if (/^EXODONCIA_INDICADA|DIENTE_EXTRAIDO|DIENTE_SIN_ERUPCIONAR|PROTESIS_FIJA_PONTICO|DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL|PROVISIONAL|DIENTE_REMP_PROTESIS_REMOVIBLE|DIENTE_EN_ERUPCION|DIENTE_PARCIALMENTE_ERUPCIONADO|SELLANTE_EN_BOCA|IMPLANTE$/.test(data.selected)) {
                onClickTooth(SVG.get(idContainer + '-' + value), data);
                count += 7;
            } else {
                for (i = 1; i < 8; i++) {
                    values = json[++count].split('=');
                    value = searchValue(data, values[1]);
                    SVG.get(idContainer + '-z' + i).data({selected: value});
                    data.selected = value;
                    onClickTooth(SVG.get(idContainer + '-z' + i), data);
                }
            }
        }
    });
    $(data.idJsonField).val(JSON.stringify(json));
    return true;
}
function searchValue(data, searchValue) {
    var value = 'LIMPIAR';
    $.each(Object.keys(data.values), function (index, key) {
        if (searchValue === data.values[key].value) {
            value = key;
            return false;
        }
    });
    return value;
}
function drawBaseTooth(container, height, onClick, data) {
    var idContainer = $(container).attr('id');
    var values = ['EXODONCIA_INDICADA', 'DIENTE_EXTRAIDO', 'DIENTE_SIN_ERUPCIONAR', 'PROTESIS_FIJA_PONTICO', 'DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL', 'PROVISIONAL', 'DIENTE_REMP_PROTESIS_REMOVIBLE', 'DIENTE_EN_ERUPCION', 'DIENTE_PARCIALMENTE_ERUPCIONADO', 'SELLANTE_EN_BOCA', 'IMPLANTE', 'z1', 'z2', 'z3', 'z4', 'z5', 'z6', 'z7'];
    var svg = '<?xml version="1.0" encoding="UTF-8" standalone="no"?><svg xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:cc="http://creativecommons.org/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" width="60" height="80" viewBox="0 0 15.874999 21.166667" version="1.1" id="svg8" inkscape:version="0.91 r13725" sodipodi:docname="dienteNormal.svg"> <defs id="defs2" /> <sodipodi:namedview id="base" pagecolor="#ffffff" bordercolor="#666666" borderopacity="1.0" inkscape:pageopacity="0.0" inkscape:pageshadow="2" inkscape:zoom="5.6568542" inkscape:cx="10.023799" inkscape:cy="35.991718" inkscape:document-units="mm" inkscape:current-layer="layer1" showgrid="true" units="px" showguides="false" inkscape:window-width="1366" inkscape:window-height="705" inkscape:window-x="-8" inkscape:window-y="-8" inkscape:window-maximized="1" gridtolerance="10" inkscape:snap-text-baseline="false"> <inkscape:grid type="xygrid" id="grid4500" /> </sodipodi:namedview> <metadata id="metadata5"> <rdf:RDF> <cc:Work rdf:about=""> <dc:format>image/svg+xml</dc:format> <dc:type rdf:resource="http://purl.org/dc/dcmitype/StillImage" /> <dc:title /> </cc:Work> </rdf:RDF> </metadata> <g inkscape:label="Capa 1" inkscape:groupmode="layer" id="layer1" transform="translate(0,-275.83332)"> <path style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" d="m 3.0876671,280.7124 c -0.021873,0.0176 -0.1107424,0.0958 -0.1415934,0.12196 0.02037,-0.0159 0.039379,-0.0329 0.059944,-0.0486 z m 1.6435039,7.94315 c -0.6027424,-0.58962 -0.9960673,-1.35471 -1.0270041,-2.24527 0.00169,-0.7902 0.2921288,-1.55921 0.8288898,-2.19469 -0.09197,-0.18513 -0.1767369,-0.35501 -0.273885,-0.55088 -0.6054648,-1.22076 -0.9429845,-1.98358 -1.3229167,-2.82205 -1.766234,1.3791 -2.79123037,3.41612 -2.79311101,5.56762 -4.237e-5,2.25961 1.10513171,4.27849 2.83858631,5.61206 0.3622349,-0.80054 0.9345491,-1.78709 1.5161864,-2.95981 z" id="z3" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccccccccccc" /> <path style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" d="m 7.9530029,276.53105 c -5.9975374,-0.0829 -6.8586929,0.15234 -5.0167481,4.21732 a 7.7945373,7.2707428 0 0 1 0.1560629,-0.12764 7.7945373,7.2707428 0 0 1 0.633553,-0.42272 7.7945373,7.2707428 0 0 1 0.6759278,-0.36173 7.7945373,7.2707428 0 0 1 0.7110677,-0.29714 7.7945373,7.2707428 0 0 1 0.7394898,-0.22893 7.7945373,7.2707428 0 0 1 0.7601603,-0.15916 7.7945373,7.2707428 0 0 1 0.7735962,-0.0873 7.7945373,7.2707428 0 0 1 0.5513874,-0.0186 7.7945373,7.2707428 0 0 1 0.778247,0.0367 7.7945373,7.2707428 0 0 1 0.7704958,0.10852 7.7945373,7.2707428 0 0 1 0.7544753,0.17984 7.7945373,7.2707428 0 0 1 0.732255,0.24908 7.7945373,7.2707428 0 0 1 0.70125,0.31626 7.7945373,7.2707428 0 0 1 0.664559,0.37982 7.7945373,7.2707428 0 0 1 0.607715,0.43098 c 1.848567,-3.8901 0.942092,-4.13324 -4.9934941,-4.21525 z" id="z1" inkscape:connector-curvature="0" /> <ellipse style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" id="z4" cx="7.9769149" cy="286.38959" rx="4.2333331" ry="3.7041669" /> <path inkscape:connector-curvature="0" id="z7" d="m 7.9170143,296.23764 c -5.997537,0.0829 -6.858693,-0.15234 -5.016748,-4.21732 a 7.7945373,7.2707428 0 0 0 0.156063,0.12764 7.7945373,7.2707428 0 0 0 0.633553,0.42272 7.7945373,7.2707428 0 0 0 0.675928,0.36173 7.7945373,7.2707428 0 0 0 0.711068,0.29714 7.7945373,7.2707428 0 0 0 0.739489,0.22893 7.7945373,7.2707428 0 0 0 0.760161,0.15916 7.7945373,7.2707428 0 0 0 0.773596,0.0873 7.7945373,7.2707428 0 0 0 0.551387,0.0186 7.7945373,7.2707428 0 0 0 0.778247,-0.0367 7.7945373,7.2707428 0 0 0 0.770496,-0.10852 7.7945373,7.2707428 0 0 0 0.7544757,-0.17984 7.7945373,7.2707428 0 0 0 0.732255,-0.24908 7.7945373,7.2707428 0 0 0 0.701249,-0.31626 7.7945373,7.2707428 0 0 0 0.664559,-0.37982 7.7945373,7.2707428 0 0 0 0.607715,-0.43098 c 1.848567,3.8901 0.942092,4.13324 -4.9934937,4.21525 z" style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" /> <path inkscape:connector-curvature="0" id="z2" d="m 7.9374997,279.13945 a 7.7945373,7.2707428 0 0 0 -0.551387,0.0186 7.7945373,7.2707428 0 0 0 -0.773596,0.0873 7.7945373,7.2707428 0 0 0 -0.760161,0.15916 7.7945373,7.2707428 0 0 0 -0.739489,0.22893 7.7945373,7.2707428 0 0 0 -0.711068,0.29714 7.7945373,7.2707428 0 0 0 -0.675928,0.36173 7.7945373,7.2707428 0 0 0 -0.633553,0.42272 7.7945373,7.2707428 0 0 0 -0.156063,0.12764 c 0.379932,0.83847 0.717452,1.60129 1.322917,2.82205 0.09715,0.19587 0.181919,0.36574 0.273885,0.55087 a 4.233333,3.7041669 0 0 1 3.404443,-1.50947 4.233333,3.7041669 0 0 1 3.3522513,1.44591 c 0.06956,-0.13245 0.131785,-0.24973 0.204121,-0.38758 0.663258,-1.26395 1.048053,-2.07248 1.452625,-2.92385 a 7.7945373,7.2707428 0 0 0 -0.607715,-0.43098 7.7945373,7.2707428 0 0 0 -0.664559,-0.37982 7.7945373,7.2707428 0 0 0 -0.701249,-0.31626 7.7945373,7.2707428 0 0 0 -0.732255,-0.24908 7.7945373,7.2707428 0 0 0 -0.7544763,-0.17984 7.7945373,7.2707428 0 0 0 -0.770496,-0.10852 7.7945373,7.2707428 0 0 0 -0.778247,-0.0367 z" style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" /> <path style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" d="m 7.941609,293.66813 c -0.1766857,-1.3e-4 -0.3533112,-0.006 -0.5295449,-0.0186 -0.2489942,-0.0171 -0.4969545,-0.0462 -0.7429516,-0.0873 -0.245929,-0.0412 -0.4895828,-0.0943 -0.7300488,-0.15916 -0.2405131,-0.0648 -0.4775411,-0.14117 -0.7101957,-0.22893 -0.2325912,-0.0879 -0.4605094,-0.1871 -0.6829005,-0.29714 -0.2224584,-0.10999 -0.4391132,-0.23072 -0.6491525,-0.36173 -0.2099454,-0.13097 -0.4130176,-0.27205 -0.6084561,-0.42272 -0.050541,-0.0419 -0.1005061,-0.0845 -0.1498809,-0.12764 0.3648818,-0.83847 0.6890316,-1.60129 1.2705124,-2.82205 0.093302,-0.19587 0.1747126,-0.36574 0.2630356,-0.55087 0.7649704,0.94761 1.9784799,1.50785 3.269583,1.50947 1.2612183,-9.2e-4 2.450553,-0.53507 3.219458,-1.44591 0.06681,0.13245 0.126565,0.24973 0.196036,0.38758 0.636984,1.26395 1.006536,2.07248 1.395082,2.92385 -0.187183,0.15283 -0.381964,0.29667 -0.583642,0.43098 -0.206091,0.13686 -0.419101,0.26363 -0.638234,0.37982 -0.218923,0.11626 -0.443694,0.22181 -0.67347,0.31626 -0.229993,0.0944 -0.464702,0.1775 -0.703248,0.24908 -0.2382938,0.0716 -0.4801253,0.13166 -0.7245891,0.17984 -0.2446411,0.0481 -0.4916078,0.0843 -0.7399745,0.10852 -0.2483537,0.0243 -0.4978048,0.0366 -0.7474184,0.0367 z" id="z6" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccccccccccccccccccccc" /> <path sodipodi:nodetypes="ccccccccccccc" inkscape:connector-curvature="0" id="z5" d="m 12.804571,280.7124 c 0.02187,0.0176 0.110742,0.0958 0.141593,0.12196 -0.02037,-0.0159 -0.03938,-0.0329 -0.05994,-0.0486 z m -1.643504,7.94315 c 0.602742,-0.58962 0.996067,-1.35471 1.027004,-2.24527 -0.0017,-0.7902 -0.292129,-1.55921 -0.82889,-2.19469 0.09197,-0.18513 0.176737,-0.35501 0.273885,-0.55088 0.605465,-1.22076 0.942985,-1.98358 1.322917,-2.82205 1.766234,1.3791 2.79123,3.41612 2.793111,5.56762 4.2e-5,2.25961 -1.105132,4.27849 -2.838586,5.61206 -0.362235,-0.80054 -0.934549,-1.78709 -1.516187,-2.95981 z" style="fill:#ffffff;stroke:#000000;stroke-width:0.2;stroke-opacity:1;stroke-linecap:round;paint-order:markers fill stroke;stroke-linejoin:round" /> <text xml:space="preserve" style="font-style:normal;font-weight:normal;font-size:27.84042168px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;opacity:1;fill:#ff0000;fill-opacity:1;stroke:none;stroke-width:0.18750001;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" x="1.2096956" y="248.22733" id="EXODONCIA_INDICADA" sodipodi:linespacing="125%" transform="scale(0.84015752,1.190253)"><tspan sodipodi:role="line" id="tspan3351" x="1.2096956" y="248.22733">x</tspan></text> <text xml:space="preserve" style="font-style:normal;font-weight:normal;font-size:21.57991409px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;fill:#ff0000;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;" x="1.7418071" y="257.80377" id="DIENTE_PARCIALMENTE_ERUPCIONADO" sodipodi:linespacing="125%" transform="scale(0.87240099,1.1462619)"><tspan sodipodi:role="line" id="tspan4155" x="1.7418071" y="257.80377">A</tspan></text> <g transform="matrix(0.87240098,0,0,1.1462619,18.457225,0.71356312)" style="font-style:normal;font-weight:normal;font-size:21.57991409px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;fill:#ff0000;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1" id="DIENTE_EN_ERUPCION"> <path d="m -4.9563765,257.0925 -2.2233212,0 -2.1683664,-6.14786 -5.5125589,-0.0102 -2.18177,6.15806 -2.11795,0 5.71109,-15.68969 2.781786,0 z m -4.4044941,-6.16419 -2.7501744,-7.70259 -2.760712,7.70259 z" id="path4176" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccccccccccc" /> </g> <text xml:space="preserve" style="font-style:normal;font-weight:normal;font-size:25.53623962px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;fill:#ff0000;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1" x="-0.78114784" y="301.16718" id="PROVISIONAL" sodipodi:linespacing="125%" transform="scale(1.0190898,0.98126783)"><tspan sodipodi:role="line" id="tspan4180" x="-0.78114784" y="301.16718">P</tspan></text> <text xml:space="preserve" style="font-style:normal;font-weight:normal;font-size:13.01118088px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;fill:#ff0700;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1" x="12.933396" y="159.4023" id="DIENTE_EXTRAIDO" sodipodi:linespacing="125%" transform="scale(0.53928281,1.8543146)"><tspan sodipodi:role="line" id="tspan4184" x="12.933396" y="159.4023">l</tspan></text> <path style="opacity:1;fill:#0000ff;fill-opacity:1;stroke-width:0.70866144;stroke-miterlimit:4;stroke-dasharray:none" d="m 0.24094353,285.62428 0.0231796,0.39619 15.29986287,0 -0.02318,-0.39619 -15.29986353,0 z m 0.0705856,1.18856 0.0231796,0.39619 15.29934827,0 -0.02318,-0.39619 -15.2993489,0 z" id="DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL" inkscape:connector-curvature="0" /> <path style="opacity:0.98;fill:#ff8500;fill-opacity:1;stroke-width:0.70866144;stroke-miterlimit:4;stroke-dasharray:none" d="m 0.24094052,285.62428 0.02318,0.39619 15.29986348,0 -0.02318,-0.39619 -15.29986248,0 z m 0.07059,1.18856 0.02318,0.39619 15.29934848,0 -0.02318,-0.39619 -15.29934848,0 z" id="DIENTE_REMP_PROTESIS_REMOVIBLE" inkscape:connector-curvature="0"/> <path style="opacity:0.98000004;fill:#00be2b;fill-opacity:1;stroke:none;stroke-width:0.70866144;stroke-miterlimit:4;stroke-dasharray:none" d="m 0.2267352,286.1361 0,0.56111 4.1255803,0 0,-0.56111 -4.1255803,0 z m 11.2959478,0 0,0.56111 4.125581,0 0,-0.56111 -4.125581,0 z" id="DIENTE_SIN_ERUPCIONAR" inkscape:connector-curvature="0" /> <text xml:space="preserve" style="font-style:normal;font-weight:normal;font-size:23.43538284px;line-height:125%;font-family:sans-serif;letter-spacing:0px;word-spacing:0px;fill:#0000ff;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;" x="0.12967761" y="284.71082" id="SELLANTE_EN_BOCA" sodipodi:linespacing="125%" transform="scale(0.9642996,1.0370221)"><tspan sodipodi:role="line" id="tspan4299" x="0.12967761" y="284.71082">S</tspan></text> <path style="opacity:0.98000004;fill:#ffff00;fill-opacity:1;stroke:none;stroke-width:0.16526595;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" d="m 0.1761363,282.38742 0.0178555,8.05851 a 5.3322595,4.0291561 0 0 0 4.6241438,-2.01785 5.3322595,4.0291561 0 0 0 0.5721474,-1.0976 l 5.094429,0 a 5.3322595,4.0291561 0 0 0 0.572147,1.0976 5.3322595,4.0291561 0 0 0 4.624144,2.01785 l 0.01786,-8.05851 a 5.3322595,4.0291561 0 0 0 -4.633404,2.00598 5.3322595,4.0291561 0 0 0 -0.575455,1.10947 l -5.1050109,0 A 5.3322595,4.0291561 0 0 0 4.8095333,284.3934 5.3322595,4.0291561 0 0 0 0.17612883,282.38742 Z" id="PROTESIS_FIJA_PONTICO" inkscape:connector-curvature="0" /> <path style="opacity:1;fill:#81817e;fill-opacity:1;stroke:none;stroke-width:0.15277502;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" d="m 5.3638244,276.31306 1.1210044,1.32354 0,15.65756 2.6400825,0 0,-15.44847 0.033517,-0.0396 1.2643527,-1.49308 -2.5292207,0 -2.5297359,0 z m 1.180818,17.00314 0.6301136,1.53943 0.6301145,1.53941 0.6301135,-1.53941 0.6301135,-1.53943 -1.260227,0 -1.2602281,0 z" id="IMPLANTE" inkscape:connector-curvature="0"/> </g></svg>';
    $.each(values, function (index, item) {
        svg = svg.replace(item, idContainer + '-' + item);
    });
    var draw = SVG(container).size(60, height);
    draw.svg(svg);
    $.each(values, function (index, item) {
        if (onClick) {
            SVG.get(idContainer + '-' + item)
                    .style({'cursor': 'pointer'})
                    .on('click', function () {
                        onClick(this, data);
                    });
        }
    });
    return draw;
}
function drawTooth(container, numTooth, dataTooth, data) {
    var idContainer = $(container).attr('id').replace(/-.+$/, '');
    var draw = drawBaseTooth(container, 100, onClickTooth, data);
    var rect = draw.rect(60, 20);
    rect.attr({id: idContainer + '-d'})
            .style('fill:none')
            .move(0, 80)
            .radius(3)
            .data({numTooth: numTooth, dataTooth: dataTooth, selected: 'LIMPIAR'});
    for (i = 1; i < 8; i++) {
        SVG.get(idContainer + '-z' + i).data({selected: 'LIMPIAR'});
    }
    var text = draw.text(numTooth);
    text.move(23, 85);
    draw.scale(0.9, 0.9);
}
function onClickTooth(element, data) {
    var idContainer = element.attr('id').replace(/-.+$/, '');
    cleanTooth(idContainer);
    if (/^EXODONCIA_INDICADA|DIENTE_EXTRAIDO|DIENTE_SIN_ERUPCIONAR|PROTESIS_FIJA_PONTICO|DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL|PROVISIONAL|DIENTE_REMP_PROTESIS_REMOVIBLE|DIENTE_EN_ERUPCION|DIENTE_PARCIALMENTE_ERUPCIONADO|SELLANTE_EN_BOCA|IMPLANTE$/.test(data.selected)) {
        cleanZones(idContainer, /^PROTESIS_FIJA_PONTICO|IMPLANTE$/.test(data.selected));
        SVG.get(idContainer + '-' + data.selected).style('display', '');
        if ($('#' + idContainer + '-d').length) {
            SVG.get(idContainer + '-d').data({selected: data.selected});
        }
    } else {
        for (j = 1; j < 8; j++) {
            SVG.get(idContainer + '-z' + j)
                    .style({'stroke': '000000'});
        }
        element.style({'fill': data.values[data.selected].color})
                .data({selected: data.selected});
    }
    if (/^odo$/.test(data.idBaseContainer)) {
        setTimeout(function () {
            calcData(data);
        }, 10);
    }
}
function calcData(data) {
    var sanos = 0;
    var obturados = 0;
    var cariados = 0;
    var perdidos = 0;
    $.each($(data.idMasterContainer + ' table:last').find((adulto ? '.adu' : '.nin') + ' td'), function (index, td) {
        var idContainer = $(td).attr('id');
        if ($('#' + idContainer + '-d').length) {
            var sano = true;
            var aux = SVG.get(idContainer + '-d').data('selected');
            if (aux !== 'LIMPIAR') {
                perdidos += (/^DIENTE_EXTRAIDO|EXODONCIA_INDICADA$/.test(aux) ? 1 : 0);
                obturados += (/^PROTESIS_FIJA_PONTICO|DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL|PROVISIONAL|DIENTE_REMP_PROTESIS_REMOVIBLE|IMPLANTE$/.test(aux) ? 1 : 0);
                sano = false;
            } else {
                for (j = 1; j < 8; j++) {
                    var aux = SVG.get(idContainer + '-z' + j).data('selected');
                    sano = sano && /^LIMPIAR$/.test(aux);
                    if (aux !== 'LIMPIAR') {
                        obturados += (!/^CARIES$/.test(aux) ? 1 : 0);
                        cariados += (/^CARIES$/.test(aux) ? 1 : 0);
                        break;
                    }
                }
            }
            sanos += (sano ? 1 : 0);
        }
    });
    $(data.idSanos).val(sanos);
    $(data.idObturados).val(obturados);
    $(data.idPerdidos).val(perdidos);
    $(data.idCariados).val(cariados);
}
function cleanTooth(idContainer) {
    var values = ['EXODONCIA_INDICADA', 'DIENTE_EXTRAIDO', 'DIENTE_SIN_ERUPCIONAR', 'PROTESIS_FIJA_PONTICO', 'DIENTE_REMP_PROTESIS_TOTAL_O_PARCIAL', 'PROVISIONAL', 'DIENTE_REMP_PROTESIS_REMOVIBLE', 'DIENTE_EN_ERUPCION', 'DIENTE_PARCIALMENTE_ERUPCIONADO', 'SELLANTE_EN_BOCA', 'IMPLANTE'];
    $.each(values, function (index, value) {
        SVG.get(idContainer + '-' + value).style('display', 'none');
        if ($('#' + idContainer + '-d').length) {
            SVG.get(idContainer + '-d').data({selected: 'LIMPIAR'});
        }
    });
}
function cleanZones(idContainer, cleanStrokeZone) {
    for (j = 1; j < 8; j++) {
        SVG.get(idContainer + '-z' + j)
                .data({selected: 'LIMPIAR'})
                .style({'fill': '#ffffff', 'stroke': cleanStrokeZone ? 'ffffff' : '000000'});
    }
}