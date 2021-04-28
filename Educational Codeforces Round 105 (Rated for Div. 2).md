### Educational Codeforces Round 105 (Rated for Div. 2)

#### A

* 大致题意：给定由 $A,B,C$ 组成的字符串，将 $A,B,C$ 分别替换成 '(' 或者 ')' ，并且要求同一个字母只能替换成同一种括号类型，要求括号能够相互匹配，问是否能够实现？
* 题解：暴力枚举每一种替换情况然后作判断即可，复杂度 $O(8n)$

#### B

* 大致题意：给定一个方格正方形，现在要寻找一种给所有靠近边的方格涂色的方式，需要保证涂色结束后，上下左右四条边分别满足有 $U,D,L,R$ 个格子被涂色。问是否存在这样的方式。
*  题解：~~一看就是专门毒我的题~~，实际上很简单，考虑只有边上有 $n-1$ 或者 $n$ 个格子被涂色，才会有可能对其他边有影响，所以可以分别考上下两个格子和左右两个格子，如果一条边要涂 $n$ 个格子，需要给与它靠近的两条边的格子数减一，如果一条边要涂 $n-1$ 个格子，需要给两边中格子数量最多的边得格子数量减一，每处理完相对的两条边后，判断临近两条边是否小于 0，若小于 0，则不成立，否则一定是成立的。

#### C

* 大致题意：给定 $n$ 个箱子，$m$ 个目标位置，这些箱子和目标位置都位于坐标轴上的某一个位置，一个人从坐标原点出发，向左或者向右走，如果人在移动过程中碰到了一个箱子堵住路，会把箱子往子集移动的方向以东，同理一个箱子也可以移动另一个箱子。问人怎样移动才能让更多的箱子在目标位置上。
* 这题思路很简单，但是十分考验马力，~~每次这种时候我都死了~~，做法自然是枚举，但是怎么枚举是个问题。考虑到有两个方向，不难发现两个方向等价，只需要考虑一个方向，另一个方向很容易类比得到。考虑正方向，注意到一开始就可能有处于目标位置的箱子，需要预处理，用map记录 a 数组（箱子位置），然后枚举b数组（目标位置）即可。之后考虑到 a,b 数组中的数的数据范围都很大，不可能一个单位一个单位的枚举，所以考虑每次枚举一段，注意到对于每一种覆盖情况，我们都可以通过适当的移动来使得其中一块靠近一个目标位置边界，而不改变答案值，所以考虑枚举最右边的箱子正好在一个目标位置的情况，之后用二分可以算出此时的答案，对于所有情况取个最大值即可。

```c++
#include <bits/stdc++.h>
using namespace std;
const int maxn=2e5+13;
vector<int> a,b,c,d;
map<int,int> mp;
int main(){
	int t;
	scanf("%d",&t);
	while(t--){
	a.clear(),b.clear(),c.clear(),d.clear();
	mp.clear();
	int n,m,x;
	scanf("%d%d",&n,&m);
	for(int i=1;i<=n;i++){
		scanf("%d",&x);
		if(x<0) c.push_back(-x);
		else a.push_back(x); 
	} 
	for(int i=1;i<=m;i++){
		scanf("%d",&x);
		if(x<0) d.push_back(-x);
		else b.push_back(x);
	}
	sort(c.begin(),c.end());
	sort(d.begin(),d.end());
	int r=0;
	for(int i=0;i<a.size();i++){
		mp[a[i]]=1;
	}
	for(int i=0;i<b.size();i++){
		if(mp[b[i]]) r++;
	}
	int ansl=r;
	for(int i=0;i<b.size();i++){
		if(mp[b[i]]){
			r--;
			continue;
		}
	    else{
	    	int rk1=upper_bound(a.begin(),a.end(),b[i])-a.begin();
	    	int rk2=lower_bound(b.begin(),b.end(),b[i]-rk1+1)-b.begin()+1;
	    	ansl=max(ansl,r+i+1-rk2+1);
		}
	}
	mp.clear();
	r=0;
	for(int i=0;i<c.size();i++){
		mp[c[i]]=1;
	}
	for(int i=0;i<d.size();i++){
		if(mp[d[i]]) r++;
	}
	int ansr=r;
	for(int i=0;i<d.size();i++){
		if(mp[d[i]]){
			r--;
			continue;
		}
	    else{
	    	int rk1=upper_bound(c.begin(),c.end(),d[i])-c.begin();
	    	int rk2=lower_bound(d.begin(),d.end(),d[i]-rk1+1)-d.begin()+1;
	    	ansr=max(ansr,r+i+1-rk2+1);
		}
	}
	printf("%d\n",ansl+ansr);
   }
} 
```

#### D

* 大致题意：规定一颗有权树上父节点的权值一定会大于子节点，现在给定叶节点的个数和叶节点两两之间的$lca$ 的权值，二且每个非叶子节点都至少有两个子节点，要求还原这颗树（包括每个点的权值）。
* 题解：初始我们有的是两两节点之间一共 $n^2$ 个节点，首先，每个叶节点和自身的 $lca$ 一定是自己，所以我们首先能够得到每个叶节点的权值。因为父节点权值始终大于子节点，所以可以考虑给节点对按照权值排序，显然排名越靠前的节点在树上的位置越低，所以考虑使用并查集从下往上合并，每次从左往右枚举节点对，如果两个节点已经在一个并查集中说明已经被合并过了，跳过，如果不是，如果这两个节点所在并查集的父节点权值最大值与对点对权值相同，说明一个是另一个的父节点。否则说明出现了新的祖先节点，定义一个新节点即可。因为是按顺序枚举的而且每个非叶子节点都至少有两个子节点，所以并不存在新增节点和当前两个节点直接还夹有未被枚举的节点的情况。另：排序以权值为第一关键词，还要以两个节点为第三第四关键词，否则会出现，1234 都是 5 的子节点，但是先合并 12 后合并 34，导致多出一个节点。

```c++
#include <bits/stdc++.h>
using namespace std;
const int maxn=3e5+13;
struct node{
	int val,i,j;
}tt[maxn];
int fa[maxn],ans[maxn],f[maxn];
int find(int x){
	return fa[x]==x?x:fa[x]=find(fa[x]);
}
bool cmp(node a,node b){
    if(a.val==b.val){
    	return a.i==b.i?a.j<b.j:a.i<b.i;
	}
	else return a.val<b.val;
}
int main(){
	int n,x;
	scanf("%d",&n);
	int cnt=0;
	for(int i=1;i<=n;i++){
		for(int j=1;j<=n;j++){
			scanf("%d",&x);
			if(i==j) ans[i]=x,fa[i]=i;
			else{
			    tt[++cnt]=(node){x,i,j};	
			}
		}
	}
	sort(tt+1,tt+1+cnt,cmp);
	for(int i=1;i<=cnt;i++){
		int f1=find(tt[i].i),f2=find(tt[i].j),now=tt[i].val;
		if(f1==f2) continue;
		if(max(ans[f1],ans[f2])==now){
			if(ans[f1]>ans[f2]){
				fa[f2]=f1;
				f[f2]=f1;
			}
			else{
				fa[f1]=f2;
				f[f1]=f2;
			}
		}
		else if(max(ans[f1],ans[f2])<now){
			n++;
			fa[n]=n;
			f[f1]=f[f2]=n;
			fa[f1]=fa[f2]=n;
			ans[n]=now;
		}
	}
	printf("%d\n",n);
	for(int i=1;i<=n;i++) printf("%d ",ans[i]);
	puts("");
	int rt=0;
	for(int i=1;i<=n;i++){
		if(find(i)==i){
			rt=i;
			break;
		}
	}
	printf("%d\n",rt);
	for(int i=1;i<=n;i++){
		if(i!=rt){
			printf("%d %d\n",i,f[i]);
		}
	}
}
```



#### E

* 大致题意：动态操作一张图，图上每一条边都有一个字母，每次可以选择在图上加边或者在图上减边，查询操作为，给定数字 $k$ ，问图上是否存在一个有 $k$ 个点的路径，使得正着走和反着走得到的字母序列完全相同？
* 题解：假题，对于每个查询，如果 $k$ 是奇数，只要判断图上是否存在连接两个点的两条反向边即可，可以证明这是上述条件的充要条件。比较显然，如果k是偶数，只要判断图上是否存在连接两个点的两条同权值的反向边，同理，这也是条件成立的充要条件，简证：如果图上现在存在连接两个点的两条同权值的反向边，假设为$u,v$，则路径 $u\to v \to u \to v$ 即满足条件。如果图上存在题目所说的路径，$v_1 \to v_2 \to v_3……\to v_{2n}$ 由条件知道，它与 $v_{2n} \to v_{2n-1} \to v_{2n-2}……\to v_{1}$ 产生相同的字母序列，则必有  $v_{n+1}$ 与 $v_n$ 之间存在两条同权值的反向边，得证。具体操作开一个 map 维护加边和删边即可。

