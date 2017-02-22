#define bitRead(value, bit) (((value) >> (bit)) & 0x01)
#define KeeLoq_NLF (0x3A5C742E)

static ulong encrypt(const uint data, const uint _keyLow, const uint _keyHigh) {
  uint x = data;
  uint r;
  int keyBitNo, index;
  uint keyBitVal,bitVal;

  for (r = 0; r < 528; r++)
  {
    keyBitNo = r & 63;
    if(keyBitNo < 32)
      keyBitVal = bitRead(_keyLow,keyBitNo);
    else
      keyBitVal = bitRead(_keyHigh, keyBitNo - 32);
    index = 1 * bitRead(x,1) + 2 * bitRead(x,9) + 4 * bitRead(x,20) + 8 * bitRead(x,26) + 16 * bitRead(x,31);
    bitVal = bitRead(x,0) ^ bitRead(x, 16) ^ bitRead(KeeLoq_NLF,index) ^ keyBitVal;
    x = (x>>1) ^ bitVal<<31;
  }
  return x;
}

__kernel void keeloq_gen(__global uint *mem, uint2 key, int num) {
    const int id = get_global_id(0);
    if (id * 3 >= num)
        return;
    uint a, b, c;
    a = 0x21220252;
    b = 0x21220254;
    c = 0x21220256;
    mem[3 * id] = encrypt(a, key.x, key.y);
    mem[3 * id + 1] = encrypt(b, key.x, key.y);
    mem[3 * id + 2] = encrypt(c, key.x, key.y);
}