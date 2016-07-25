angular.module('controlePatrimonialApp').filter('cadastro', function () {
    return function (cadastro) {
        if (!cadastro) { return ''; }

        var value = cadastro.toString().trim().replace(/^\+/, '');

        switch (value.length) 
        {
	        case 4:
	            value = value.slice(0, 3) +'-' + value.slice(3);
	            break;
	        case 5:
	            value = value.slice(0, 1) + '.' + value.slice(1,4)+ '-' + value.slice(4);
	            break;
            case 6:
                value = value.slice(0, 2) + '.' + value.slice(2,5)+ '-' + value.slice(5);
                break;
            case 7:
                value = value.slice(0, 3) + '.' + value.slice(3,6)+ '-' + value.slice(6);
                break;

            default:
                return value;
        }
        return value.trim();
    };
});