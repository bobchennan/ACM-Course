bool compile(char *code,char *lang){
	FILE *out=fopen("a.cnx","w");
	fprintf(out,"%s",code);
	fclose(out);
	int len=strlen(lang);
	if(lang[len-1]=='+')return system("g++ a.cnx -o a");
	else if(lang[len-1]=='c')return system("gcc a.cnx -o a");
	else if(lang[len-1]=='a')return system("javac a.cnx");
	else return 1;
}

