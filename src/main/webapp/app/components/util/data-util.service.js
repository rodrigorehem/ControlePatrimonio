(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .factory('DataUtils', DataUtils);

    DataUtils.$inject = ['$window'];

    function DataUtils ($window) {

        var service = {
            abbreviate: abbreviate,
            byteSize: byteSize,
            openFile: openFile,
            toBase64: toBase64
        };

        return service;

        function abbreviate (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        }

        function byteSize (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }

            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }

            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }

            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ') + ' bytes';
            }

            return formatAsBytes(size(base64String));
        }

        function openFile (type, data) {
        	/*var myWindow = $window.open("", "Download", "width=200,height=100");
        	myWindow.document.write("<p>This is 'MsgWindow'. I am 200px wide and 100px tall!</p>");
        	
        	myWindow.document.write(" <a id='dwnldLnk' download='o ficheirinho de tostas.pdf' style='display:none;' /> " +
        			"<a href='#' onclick='downloadPDF();' title='o ficheirinho de tostas.pdf'>clica aqui oh sashavore</a>");
        		
        	myWindow.downloadPDF = function downloadPDF() {

        	    var dlnk = document.getElementById('dwnldLnk');
        	    dlnk.href = data;

        	    dlnk.click();


        	    alert('toma');
        	}*/

            //$window.open('data:' + type + ';base64,' + data, '_blank', 'height=300,width=400');
        	$window.open('data:application/octet-stream;base64,' + data, '_self', 'height=300,width=400,download=t.pdf,title=oi.pdf');
        }

        function toBase64 (file, cb) {
            var fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function (e) {
                var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                cb(base64Data);
            };
        }
    }
})();
