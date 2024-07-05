#编排
THEN(a, b, THEN(c, d)); 等价 SER(a, b, c, d); // 依次执行a,b,c,d四个组件
WHEN(a, b, c); 等价 PAR(a, b, c); // 并行执行a,b,c三个组件
SWITCH(x).TO(a, b, c).DEFAULT(y); // 根据组件a，来选择执行b,c,d中的一个，x如果返回非a,b,c中的一个，则默认选择到y
