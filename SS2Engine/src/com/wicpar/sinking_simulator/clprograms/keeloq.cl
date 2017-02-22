#define bitRead(value, bit) (((value) >> (bit)) & 0x01)
#define KeeLoq_NLF (0x3A5C742E)

static uint decrypt(const uint data, const uint _keyLow, const uint _keyHigh) {
  uint x = data;
  uint r;
  int keyBitNo, index;
  uint keyBitVal,bitVal;

  for (r = 0; r < 528; r++)
  {
    keyBitNo = (15-r) & 63;
    if(keyBitNo < 32)
      keyBitVal = bitRead(_keyLow,keyBitNo);
    else
      keyBitVal = bitRead(_keyHigh, keyBitNo - 32);
    index = 1 * bitRead(x,0) + 2 * bitRead(x,8) + 4 * bitRead(x,19) + 8 * bitRead(x,25) + 16 * bitRead(x,30);
    bitVal = bitRead(x,31) ^ bitRead(x, 15) ^ bitRead(KeeLoq_NLF,index) ^ keyBitVal;
    x = (x<<1) ^ bitVal;
  }
  return x;
}

static int check(const uint a, const uint b) {
    if (a == b)
        return 0;
    if (((b & 0xFFFF) - (a & 0xFFFF)) < 7 && (a & 0xFFFF0000) == (b & 0xFFFF0000))
        return 1;
    return 0;
}

__kernel void keeloq(__global uint *encrypted, __global uint *ret, uint2 arange, uint2 brange, int num) {
    const int id = get_global_id(0);
    if (id >= num)
        return;

    uint x = arange.x - 1;
    while (++x <= arange.y)
    {
        uint y = brange.x - 1;
        while (++y <= brange.y)
        {
            uint a, b, c;
            a = decrypt(encrypted[0], x, y);
            b = decrypt(encrypted[1], x, y);
            if (check(a, b)) {
                c = decrypt(encrypted[2], x, y);
                if (check(b, c)) {
                    ret[0] = x;
                    ret[1] = y;
                   return;
               }
            }
        }
    }
    return;
}