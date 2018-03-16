push 0
lfp
push function2
lfp
push function4
lfp
push function5
lfp
push function7
push 2
push 1
push 4
push 3
push 2
push 5
push -1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lfp
lfp
lfp
push -10
add
lw
lfp
push -8
add
lw
lfp
push -8
push 1
sub
add
lw
js
lfp
push -2
add
lw
lfp
push -2
push 1
sub
add
lw
js
halt

function0:
cfp
lra
lfp
lw
push -1
add
lw
srv
sra
pop
sfp
lrv
lra
js

function1:
cfp
lra
lfp
lw
push -2
add
lw
srv
sra
pop
sfp
lrv
lra
js

function3:
cfp
lra
lfp
push 2
add
lw
lfp
push 1
add
lw
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
srv
sra
pop
pop
pop
sfp
lrv
lra
js

function2:
cfp
lra
lfp
push function3
lfp
push 1
add
lw
push -1
beq label2
push 0
b label3
label2:
push 1
label3:
push 1
beq label0
lfp
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 0
add
lw
js
print
lfp
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -2
add
lw
lfp
lw
push -2
push 1
sub
add
lw
js
lfp
push -2
add
lw
lfp
push -2
push 1
sub
add
lw
js
b label1
label0:
push -1
label1:
srv
pop
pop
sra
pop
pop
sfp
lrv
lra
js

function4:
cfp
lra
lfp
push 1
add
lw
push -1
beq label6
push 0
b label7
label6:
push 1
label7:
push 1
beq label4
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 0
add
lw
js
lfp
lfp
push 2
add
lw
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -4
add
lw
lfp
lw
push -4
push 1
sub
add
lw
js
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
b label5
label4:
lfp
push 2
add
lw
label5:
srv
sra
pop
pop
pop
sfp
lrv
lra
js

function6:
cfp
lra
lfp
lw
push 3
add
lw
push 1
beq label8
lfp
push 1
add
lw
push 1
beq label10
push 1
b label11
label10:
push 0
label11:
b label9
label8:
lfp
push 1
add
lw
label9:
srv
sra
pop
pop
sfp
lrv
lra
js

function5:
cfp
lra
lfp
push function6
lfp
push 1
add
lw
push -1
beq label14
push 0
b label15
label14:
push 1
label15:
push 1
beq label12
lfp
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 0
add
lw
js
lfp
push 2
add
lw
bleq label18
push 0
b label19
label18:
push 1
label19:
lfp
push -2
add
lw
lfp
push -2
push 1
sub
add
lw
js
push 1
beq label16
lfp
lfp
push 3
add
lw
lfp
push 2
add
lw
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -6
add
lw
lfp
lw
push -6
push 1
sub
add
lw
js
b label17
label16:
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 0
add
lw
js
lfp
lfp
push 3
add
lw
lfp
push 2
add
lw
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -6
add
lw
lfp
lw
push -6
push 1
sub
add
lw
js
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
label17:
b label13
label12:
push -1
label13:
srv
pop
pop
sra
pop
pop
pop
pop
sfp
lrv
lra
js

function7:
cfp
lra
lfp
push 1
add
lw
push -1
beq label22
push 0
b label23
label22:
push 1
label23:
push 1
beq label20
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 0
add
lw
js
b label21
label20:
push 0
label21:
lfp
push 1
add
lw
push -1
beq label26
push 0
b label27
label26:
push 1
label27:
push 1
beq label24
lfp
lfp
push -2
add
lw
lfp
lfp
push 0
lfp
push -2
add
lw
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -6
add
lw
lfp
lw
push -6
push 1
sub
add
lw
js
lfp
lw
push -8
add
lw
lfp
lw
push -8
push 1
sub
add
lw
js
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
lfp
lfp
push 1
lfp
push -2
add
lw
lfp
lfp
push 1
add
lw
lfp
push 1
add
lw
push 1
add
lw
js
lfp
lw
push -6
add
lw
lfp
lw
push -6
push 1
sub
add
lw
js
lfp
lw
push -8
add
lw
lfp
lw
push -8
push 1
sub
add
lw
js
lfp
lw
push -4
add
lw
lfp
lw
push -4
push 1
sub
add
lw
js
b label25
label24:
push -1
label25:
srv
pop
sra
pop
pop
sfp
lrv
lra
js
