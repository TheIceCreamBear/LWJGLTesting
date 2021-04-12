#include <stdio.h>
#include <math.h>
#include <chrono>
#include <random>

typedef struct Info {
    float number;
    float fastResult;
    float result;
    long fastTime;
    long time;
} Info;

float fastInverseSquareRoot(float number) {
    long i;
    float x2, y;
    const float threehalfs = 1.5F;

    x2 = number * 0.5F;
    y = number;
    i = * (long*) &y;
    i = 0x5f3759df - (i >> 1);
    y = * (float*) &i;
    y = y * (threehalfs - (x2 * y * y));

    return y;
}

int main(void) {
    // setup storage
    const int count = 10000;
    Info info[count];

    // setup random
    std::mt19937 r = std::mt19937(156467);
    std::uniform_int_distribution<int> d = std::uniform_int_distribution<int>(1000, 100000);

    // loop all indicies
    for (int i = 0; i < count; i++) {
        info[i].number = d(r); // TODO number generation with primes?

        if (info[i].number <= 0.0001f) {
            i--;
            continue;
        }

        using namespace std;

        auto start = chrono::high_resolution_clock::now();
        info[i].fastResult = fastInverseSquareRoot(info[i].number);
        auto stop = chrono::high_resolution_clock::now();
        info[i].fastTime = chrono::duration_cast<chrono::nanoseconds>(stop - start).count();

        
        start = chrono::high_resolution_clock::now();
        info[i].result = 1.0f / sqrtf(info[i].number);
        stop = chrono::high_resolution_clock::now();
        info[i].time = chrono::duration_cast<chrono::nanoseconds>(stop - start).count();    
    }

    // loop everything again and take the averages
    long fastTimeTotal = 0;
    long timeTotal = 0;
    double diffTotal = 0.0;
    for (int i = 0; i < count; i++) {
        fastTimeTotal += info[i].fastTime;
        timeTotal += info[i].time;
        
        double diff = abs(info[i].result - info[i].fastResult);
        diffTotal += diff;
        
        if (diff > 0.01 && diff > 0) {
            printf("%5d: %f & %f differ by %f on the number %f\n", i, info[i].result, info[i].fastResult, diff, info[i].number);
        }
        
        if (info[i].fastTime > info[i].time) {
            printf("%5d: The time for %f is slower for the fast algo by %fns\n", i, info[i].number, diff);
        }
    }
    
    printf("On average over %d trials,\n", count);
    printf("Fast invsqrt took %fns\n", (double) fastTimeTotal / count);
    printf("Inverse sqrt took %fns\n", (double) timeTotal / count);
    printf("And the difference in the result was %f\n", (double) diffTotal / count);

    return 0;
}
