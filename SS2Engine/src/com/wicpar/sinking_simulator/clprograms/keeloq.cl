#define bitRead(value, bit) (((value) >> (bit)) & 0x01)
#define KeeLoq_NLF (0x3A5C742E)
#define KeeLoq_NLF2 (uint2)(0x3A5C742E)

static uint2 decrypt2(const uint2 data, const uint _keyLow, const uint _keyHigh) {
    const ulong key = ((ulong)_keyHigh) << 32 | _keyLow;
    uint2 x = data;
    uint r;
    uint2 bitVal;

    #pragma unroll
    for (r = 0; r < 528; r++)
    {
        bitVal = bitRead(x,31) ^
        bitRead(x, 15) ^
        bitRead(KeeLoq_NLF2, (x & 1) | (bitRead(x,8) << 1) | (bitRead(x,19) << 2) | (bitRead(x,25) << 3) | (bitRead(x,30) << 4)) ^
        (uint)(bitRead(key, (15-r) & 63));
        x = (x<<1) | bitVal;
    }
    return x;
}

static uint decrypt(const uint data, const uint _keyLow, const uint _keyHigh) {
  const ulong key = ((ulong)_keyHigh) << 32 | _keyLow;
  uint x = data;
  uint r;

  #pragma unroll
  for (r = 0; r < 528; r++)
  {
    const uchar bitVal = bitRead(x,31) ^
    bitRead(x, 15) ^
    bitRead(KeeLoq_NLF,(x & 1) + (bitRead(x,8) << 1) + (bitRead(x,19) << 2) + (bitRead(x,25) << 3) + (bitRead(x,30) << 4)) ^
    bitRead(key, (15-r) & 63);
    x = (x<<1) | bitVal;
  }
  return x;
}

static inline int check(const uint a, const uint b) {
    return (b - a < 7 && a != b);
}

__kernel void keeloq(__global uint2 *encrypted, __global uint *ret, uint2 arange, uint2 brange, int num) {
    const int id = get_global_id(0);
    if (id >= num)
        return;
    {
        uint diff = brange.y - brange.x + 1;
        brange.x += id * diff;
        brange.y += id * diff;
    }
    uint x = arange.x - 1;
    while (++x <= arange.y)
    {
        uint y = brange.x - 1;
        while (++y <= brange.y)
        {
            const ulong key = ((ulong)y) << 32 | x;

            uint2 d = decrypt2(encrypted[0], x, y);
            if (check(d.x, d.y)) {
                uint c = decrypt(encrypted[1].x, x, y);
                if (check(d.y, c)) {
                    ret[0] = x;
                    ret[1] = y;
                   return;
               }
            }
        }
    }
    return;
}