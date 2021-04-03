#include <bits/stdc++.h>
#include <windows.h>
#define maxn 233
 
using namespace std;

char s[maxn];
int last;

int main(){
	while(scanf("%s", s) != EOF){
		int len = strlen(s), now = 0;
		bool tag = false;
		for(int i = 0;i < len;i++){
			if(s[i] == '[' || s[i] == '.') continue;
			if(s[i] == ']'){
				Sleep((now - last) * 100);
				last = now;
				tag = true;
				continue;
			}
			if(!tag){
				now = now * 10 + s[i] - '0';
				continue;
			}
			printf("%c", s[i]);
		}
		puts("");
		fflush(stdout);
	}
}
