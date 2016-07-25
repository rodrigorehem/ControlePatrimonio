angular.module('controlePatrimonialApp').filter('tel', function () {
    return function (tel) {
        if (!tel) { return ''; }

        var value = tel.toString().trim().replace(/^\+/, '');

        if (value.match(/[^0-9]/)) {
            return tel;
        }

        var city, number;

        switch (value.length) 
        {
            case 11: // +CPPP####### -> CCC (PP) ###-####
                city = value.slice(0, 2);
                number = value.slice(2);
                break;

            default:
                return tel;
        }

        number = number.slice(0, 5) + '-' + number.slice(5);

        return (" (" + city + ") " + number).trim();
    };
});