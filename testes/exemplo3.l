

var integer n1 = '1', n2 = '1';
integer temp, i;

for i = 3 to 10 do {
    temp = n1;
    n1 = n2;
    n2 = n2 + temp;

    if n1 < n2 or n2 > n1 then
        n[4] = 5;
}

writeln("O numero de fibonacci de 10 e", n2);