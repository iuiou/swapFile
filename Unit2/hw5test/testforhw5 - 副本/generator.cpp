#include <bits/stdc++.h>

using namespace std;

mt19937 mt(time(0));

void Night(){
	uniform_int_distribution<int> T(1, 2), Cnt(1, 30), F(2, 20);
	int t = T(mt), cnt = Cnt(mt);
	printf("[%d.0]Night\n", t);
	for(int i = 1;i <= cnt;i++){
		printf("[%d.0]%d-FROM-%d-TO-1\n", t, i, F(mt));
	} 
}

void Morning(){
	uniform_int_distribution<int> T(0, 20), Cnt(1, 30), F(2, 20);
	double now = T(mt) + 10;
	int cnt = Cnt(mt);
	printf("[%.1f]Morning\n", now / 10);
	for(int i = 1;i <= cnt;i++){
		now += T(mt);
		printf("[%.1f]%d-FROM-1-TO-%d\n", now / 10, i, F(mt));
	}
}

void Random(){
	uniform_int_distribution<int> T(0, 50), Cnt(1, 30), F(1, 20);
	double now = T(mt) + 10;
	int cnt = Cnt(mt);
	printf("[%.1f]Random\n", now / 10);
	for(int i = 1;i <= cnt;i++){
		now += T(mt);
		if(i == 1) now = min(now, 50.0);
		int from = F(mt), to = F(mt);
		if(from == to) from = from == 20 ? 19 : from + 1;
		printf("[%.1f]%d-FROM-%d-TO-%d\n", now / 10, i, from, to);
	}
}

int main(){
	uniform_int_distribution<int> type(1, 3);
	switch(type(mt)){
		case 1:
			Night(); 
			break;
		case 2:
			Morning();
			break;
		case 3:
			Random();
			break;		
	}
}
