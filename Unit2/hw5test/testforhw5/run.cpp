#include <bits/stdc++.h>
#define maxn 233

using namespace std;

double t;
char s[maxn];

inline vector<int> split(string s){
	vector<int> v;
	int x = -1;
	for(int i = 0;i < s.length();i++){
		if(s[i] >= '0' && s[i] <= '9'){
			if(x == -1) x = 0;
			x = x * 10 + s[i] - '0';
		}else{
			if(x != -1) v.push_back(x);
			x = -1;
		}
	}
	if(x != -1) v.push_back(x);
	return v;
}

int main(){
	for(int i = 1;;i++){
		system("generator.exe > stdin.txt");
		system("interactor.exe < stdin.txt | java -jar run.jar > stdout.txt");
		system("checker.exe > result.txt");
		freopen("result.txt", "r", stdin);
		scanf("%lf", &t);
		if(t == -1) return printf("Wrong Answer!"), 0;
		system("datacheck_student_win64.exe > result.txt");
		freopen("result.txt", "r", stdin), gets(s);
		vector<int> a = split(s);
		if(a.size() != 2) continue;
		printf("Testcase %d: Your time is %.4f. Base Time is %d. Max Time is %d.\n", i, t, a[0], a[1]);
		if(t > a[1]) return printf("Time Limit Exceeded!"), 0;
	}
}
