// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
var exampleApp=angular.module('starter', ['ionic'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
//Refrence: https://github.com/cfjedimaster/Cordova-Examples/tree/master/barcode
var resultDiv;
var upc,id,review;
document.addEventListener("deviceready", init, false);
function init() {
	document.querySelector("#startScan").addEventListener("touchend", startScan, false);
	resultDiv = document.querySelector("#results");
}

function startScan() {

	cordova.plugins.barcodeScanner.scan(
		function (result) {
            upc=result.text;
			var s = "<b><font color=blue>Result:</font></b>" + result.text + "<br/>" +
			"<b><font color=blue>Format:</font></b>" + result.format + "<br/>";
			resultDiv.innerHTML = s;
		}, 
		function (error) {
			alert("Scanning failed: " + error);
		}
	);

}
/*function auth()
{
   var login=document.getElementById('login');
 var password=document.getElementById('password');
    if(login=="admin"&&password="admin")
        {
            window.location="home.html";
        }
    else 
        alert("username and password incorrect")
}
*/
exampleApp.controller('ExampleController', function($scope, $http) {
 
    $scope.getData = function() {
        $http.get("http://api.walmartlabs.com/v1/items?apiKey=9gnk426wzsb972r7xmumfaxr&upc="+upc+"&format=json")
            .success(function(data) {
            
                $scope.name = data.items[0].name;
            $scope.price = data.items[0].salePrice;
            id=data.items[0].itemId;
            
            })
            .error(function(data) {
                //alert("ERROR");
            });
         $http.get("http://api.walmartlabs.com/v1/reviews/"+id+"?apiKey=9gnk426wzsb972r7xmumfaxr&format=json")
            .success(function(data) {
            $scope.review=data.reviews[1].reviewText;
             review=data.reviews[1].reviewText
            
            })
            .error(function(data) {
               // alert("ERROR");
            });
    }
    $scope.analysis = function() {
        $http.get("https://api.uclassify.com/v1/uclassify/sentiment/Classify?readkey=Zg3MvbzfpWBc&text="+review)
            .success(function(data) {
            
                $scope.positive = (data.positive*100).toFixed(2);
            $scope.negative = (data.negative*100).toFixed(2);
            
            
            })
            .error(function(data) {
                alert("ERROR");
            });
    }
 
});
