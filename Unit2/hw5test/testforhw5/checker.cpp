#include <bits/stdc++.h>
#define maxn 233

using namespace std;

struct Event{
	int t;
	string s;
	int i;
	
	inline bool operator < (const Event &e) const {
		return t ^ e.t ? t < e.t : i < e.i;
	}
};

vector<Event> v;
char s[maxn];
int F = 1, last = 0;
bool open = false;
vector<pair<int, int> > out[maxn], in;

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

inline void wa(){
	printf("-1");
	exit(0);
}

int main(){
	freopen("stdin.txt", "r", stdin);
	scanf("%s", s);
	int cnt = 0;
	while(scanf("%s", s) != EOF){
		int len = strlen(s), t = 0;
		bool tag = false;
		string S;
		for(int i = 0;i < len;i++){
			if(s[i] == '[' || s[i] == '.') continue;
			if(s[i] == ']'){
				tag = true;
				continue;
			}
			if(!tag){
				t = t * 10 + s[i] - '0';
				continue;
			}
			S += s[i];
		}
		v.push_back((Event){t * 1000 - 1000, S, ++cnt});
	}
	freopen("stdout.txt", "r", stdin);
	cnt = 0;
	while(gets(s) != NULL){
		int t = 0;
		bool tag = false;
		string S;
		for(int i = 0;;i++){
			if(s[i] == '[' || s[i] == '.') continue;
			if(s[i] == ']'){
				tag = true;
				continue;
			}
			if(!tag){
				if(s[i] == ' ') continue;
				t = t * 10 + s[i] - '0';
				continue;
			}
			if(!s[i]) break;
			S += s[i];
		}
		v.push_back((Event){t, S, ++cnt});
	}
	sort(v.begin(), v.end());
	for(int i = 0;i < v.size();i++){
		int now = v[i].t;
		vector<int> a = split(v[i].s);
		//printf("%d %s %d--\n", i, v[i].s.c_str(), a.size());
		bool tag = false;
		switch(v[i].s[1]){
			case 'R'://ARRIVE
				if(now - last < 4000 || abs(a[0] - F) != 1 || open) wa();
				last = now, F = a[0];
				break;
			case 'P'://OPEN
				if(a[0] != F || open) wa();
				last = now, open = true;
				break;
			case 'N'://IN
				if(a[1] != F || !open) wa();
				for(int j = 0;j < out[F].size();j++){
					pair<int, int> p = out[F][j]; 
					if(p.first == a[0]){
						in.push_back(p);
						out[F].erase(out[F].begin() + j);
						tag = true;
						break;
					}
				}
				if(!tag) wa();
				break;
			case 'U'://OUT
				if(a[1] != F || !open) wa();
				for(int j = 0;j < in.size();j++){
					pair<int, int> p = in[j];
					if(p.first == a[0] && p.second == F){
						in.erase(in.begin() + j);
						tag = true;
						break;
					}
				}
				if(!tag) wa();
				break;
			case 'L'://CLOSE
				if(a[0] != F || !open || now - last < 4000) wa();
				last = now, open = false;
				break;
			default:
				out[a[1]].push_back({a[0], a[2]});
				break;					
		}
	}
	printf("%.4f", last / 10000.0);
}
