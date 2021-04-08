#include <bits/stdc++.h>
using namespace std;
int main(){
	srand(time(0));
	printf("Random\n");
	for(int i=1;i<=50;i++){
		int a=0,b=0;
	    while(a==b){
	    	a=rand()%20+1;
	    	b=rand()%20+1;
		} 
		printf("%d-FROM-%d-TO-%d\n",i,a,b);
	}
} 
