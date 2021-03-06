@test
shared void bytes() {
  check(127.byte==Byte(127), "byte 127");
  check(Byte(-1).unsigned==255, "byte -1");
  check(Byte(0).unsigned==0, "byte 0");
  check(Byte(255).unsigned==255, "byte 255");
  check(Byte(256).unsigned==0, "byte 256");
  check(Byte(65535).unsigned==255, "byte 65535");
  check(Byte(-1).signed==-1, "byte -1");
  check(Byte(0).signed==0, "byte 0");
  check(Byte(255).signed==-1, "byte 255");
  check(Byte(256).signed==0, "byte 256");
  check(Byte(65535).signed==-1, "byte 65535");
  check(Byte(0).or(Byte(1))==Byte(1), "byte 0|1");
  check(Byte(15).or(Byte(5))==Byte(15), "byte 15|5");
  check(Byte(7).and(Byte(1))==Byte(1), "byte 7&2");
  check(Byte(7).and(Byte(64))==Byte(0), "byte 7&64");
  check(Byte(3).xor(Byte(1))==Byte(2), "byte 3^1");
  check(Byte(3).xor(Byte(2))==Byte(1), "byte 3^2");
  check(Byte(5).xor(Byte(10))==Byte(15), "byte 5^10");
  check(Byte(1).negated==Byte(255), "byte 1.negated");
  check(Byte(255).negated==Byte(1), "byte 255.negated");
  check(Byte(127).negated==Byte(129), "byte 127.negated");
  check(Byte(0).negated==Byte(0), "byte 0.negated");
  check(Byte(127)+Byte(128)==Byte(255), "byte 127+128");
  check(Byte(250)+Byte(10)==Byte(4), "byte 250+10");
  check(Byte(499)+Byte(1)==Byte(244), "byte 499+1");
  check(Byte(15).flip(3)==Byte(7), "15 flip 3");
  check(Byte(255).flip(4)==Byte(239), "255 flip 4");
  check(Byte(1).flip(3)==Byte(9), "1 flip 3");
  check(Byte(15).get(3), "15 get 4");
  check(!Byte(15).get(4), "15 get 5");
  check(Byte(7).set(3,true)==Byte(15), "7.set(4,t)");
  check(Byte(128).set(7,false)==Byte(0), "128.set(7,f)");
  check(Byte(15).not==Byte(240), "byte !15");
  check(Byte(127).not==Byte(128), "byte !127");
  check(Byte(170).not==Byte(85), "byte !170");
  check(Byte(120).hash==120, "byte.hash 120");
  check(Byte(170).hash==-86, "byte.hash 170");
  check(Byte(85).string=="85", "byte.string 85");
  check(Byte(255).string=="255", "byte.string 255");
  check(Byte(0).leftLogicalShift(3) == Byte(0), "0<<3");
  check(Byte(1).leftLogicalShift(3) == Byte(8), "1<<3");
  check(Byte(255).leftLogicalShift(9) == Byte(254), 
      "255<<9 " + Byte(255).leftLogicalShift(9).string);
  check(Byte(0).rightLogicalShift(3) == Byte(0), "0>>>3");
  check(Byte(8).rightLogicalShift(3) == Byte(1), "8>>>3");
  check(Byte(128).rightLogicalShift(1)==Byte(64), "128>>>1");
  check(Byte(255).rightLogicalShift(9) == Byte(127), 
      "255>>>9 " + Byte(255).rightLogicalShift(9).string);
  check(Byte(255).rightLogicalShift(4) == Byte(15), "255>>>4");
  check(Byte(0).rightArithmeticShift(3) == Byte(0), "0>>3");
  check(Byte(64).rightArithmeticShift(1)==Byte(32), "64>>1");
  check(Byte(255).rightArithmeticShift(1)==Byte(255), 
      "255>>1 " + Byte(255).rightArithmeticShift(1).string);
  check(Byte(255).rightArithmeticShift(4)==Byte(255), 
      "255>>4 " + Byte(255).rightArithmeticShift(4).string);
  check(Byte(255).rightArithmeticShift(9)==Byte(255), 
      "255>>9 " + Byte(255).rightArithmeticShift(9).string);
  variable Byte b = Byte(1);
  b++;
  check(b==Byte(2), "1++");
  b--;
  check(b==Byte(1), "2--");
  b--;
  check(b==Byte(0), "1--");
  b--;
  check(b==Byte(255), "0--");
  b++;
  check(b==Byte(0), "255++");
  check(Byte(1)..Byte(3) == [Byte(1), Byte(2), Byte(3)], "1..3");
  check(Byte(255)..Byte(1) == [Byte(255), Byte(0), Byte(1)], "255..1");
  check(every { for (bit in 0:8) $11111111.byte.get(bit) }, "All bits should be set #6315");
  
  value byt1 = ~#F0.byte;
  value byt2 = byt1|#F0.byte;
  value byt3 = ~(byt2&$10101010.byte);
  check(byt1==#F.byte, "bitwise complement");
  check(byt2==#FF.byte, "bitwise or");
  check(byt3==$01010101.byte, "bitwise and");
  
  variable value bytv = $101.byte;
  bytv |= $10.byte;
  bytv &= $11.byte;
  check(bytv==$11.byte, "bitwise assignment");
}
