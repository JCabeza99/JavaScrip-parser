grammar JS_PDL;

@parser::header {import com.myProject.JavaScriptPDL.Processor.*;}
@lexer::header {import com.myProject.JavaScriptPDL.Processor.Error;}

@parser::members {
			ListaToken tokens = new ListaToken();
			List<SymbolTable> tablas = new ArrayList<>();
			String parse = "Descendente ";
			SymbolTable tablaSG = new SymbolTable(null);
			SymbolTable tablaSA = tablaSG;
			int desplA = 0;
			int desplG = 0;
			
}

e
	returns[Symbol eSymbol]
	@init {$eSymbol = new Symbol();}:
	{parse+="1 ";} r ep[$r.rSymbol.getTipo()]{
		if($ep.epSymbol.getTipo() == Tipo.VACIO) {
			$eSymbol.setTipo($r.rSymbol.getTipo());
		} else {
			$eSymbol.setTipo($ep.epSymbol.getTipo());
		}
		};

ep[Tipo eTipo]
	returns[Symbol epSymbol]
	@init {$epSymbol = new Symbol();}:
	{parse+="2 ";} NOT_EQ r ep[$r.rSymbol.getTipo()]{
		if($r.rSymbol.getTipo()!=eTipo){
			notifyErrorListeners(Error.ERROR_OP_LOGICA1.getMsg());
		} else if ( $ep.epSymbol.getTipo()!=Tipo.VACIO) {
			notifyErrorListeners(Error.ERROR_OP_LOGICA2.getMsg());
		} else {
			$epSymbol.setTipo(Tipo.BOOL);
		}
	}
	| {parse+="3 "; $epSymbol.setTipo(Tipo.VACIO);};

r
	returns[Symbol rSymbol]
	@init {$rSymbol = new Symbol();}:
	{parse+="4 ";} u rp[$u.uSymbol.getTipo()] {$rSymbol.setTipo($u.uSymbol.getTipo());};

rp[Tipo rTipo]
	returns[Symbol rpSymbol]
	@init {$rpSymbol = new Symbol();}:
	{parse+="5 ";} ADD u rp[$u.uSymbol.getTipo()] {if($u.uSymbol.getTipo()!=Tipo.INT || ($rp.rpSymbol.getTipo()!=Tipo.INT && $rp.rpSymbol.getTipo()!=Tipo.VACIO) || rTipo != Tipo.INT)
	{notifyErrorListeners(Error.ERROR_SUMA_ENTEROS.getMsg());}  else {
			$rpSymbol.setTipo(Tipo.INT);
		}
		} 
	| {parse+="6 "; $rpSymbol.setTipo(Tipo.VACIO);};

u
	returns[Symbol uSymbol]
	@init {$uSymbol = new Symbol();}:
	NOT {parse+="7 ";} u {if($u.uSymbol.getTipo() != Tipo.BOOL){
		notifyErrorListeners(Error.ERROR_EXPR_NO_BOOL.getMsg());
		}
		$uSymbol.setTipo($u.uSymbol.getTipo());
		}
	| {parse+="8 ";} L_PARENT e R_PARENT {$uSymbol.setTipo($e.eSymbol.getTipo());}
	| {parse+="9 ";} ID {int index = tokens.getLastIndex();} up {Symbol id = SymbolTable.find(tablaSA,tablaSG, $ID.text);
		if(id == null && !$up.upSymbol.isFunction()){
			id = new Symbol(Tipo.INT,desplG);
			id.setLexema($ID.text);
			$uSymbol.setTipo(Tipo.INT);
			tokens.setLastToken(index,tablaSG.insertar($ID.text, id));
			desplG += Tipo.INT.getSize();
			desplA += tablaSG == tablaSA? Tipo.INT.getSize() : 0;
		} else if (id != null && id.isFunction() && $up.upSymbol.isFunction()){
			tokens.setLastToken(index,SymbolTable.findPos(tablaSA,tablaSG,$ID.text));
			if(id.getParam().equals($up.upSymbol.getParam())) {
				$uSymbol.setTipo(id.getRetorno());
			} else {
				notifyErrorListeners(Error.ERROR_PARAMETROS_FUNC.getMsg());
			}
		} else if (id != null && !id.isFunction() && !$up.upSymbol.isFunction()){
			tokens.setLastToken(index,SymbolTable.findPos(tablaSA,tablaSG,$ID.text));
			$uSymbol.setTipo(id.getTipo());
		} else {
			notifyErrorListeners(Error.ERROR_NO_FUNC.getMsg());
		}
	}
	| {parse+="12 ";} INT_LITERAL {$uSymbol.setTipo(Tipo.INT);
		int value = Integer.parseInt($INT_LITERAL.text);
		if (value > 32767) {
			notifyErrorListeners(Error.ERROR_INT_MAX.getMsg());
		}
	}
	| {parse+="13 ";} STRING_LITERAL {$uSymbol.setTipo(Tipo.STRING);
		
		int size = $STRING_LITERAL.text.length();
		if (size > 64) {
			notifyErrorListeners(Error.ERROR_STRING_MAX.getMsg());
		}

	}
	| {parse+="14 ";} TRUE {$uSymbol.setTipo(Tipo.BOOL);}
	| {parse+="15 ";} FALSE {$uSymbol.setTipo(Tipo.BOOL);};

up
	returns[Symbol upSymbol]
	@init {$upSymbol = new Symbol();}:
	{parse+="10 ";} L_PARENT l R_PARENT {
		$upSymbol.setFunction(true);
		$upSymbol.setParam($l.lSymbol.getParam());}
	| {
		parse+="11 ";
		$upSymbol.setFunction(false);
	};

s
	returns[Symbol sSymbol]
	@init {$sSymbol = new Symbol();}:
	{parse+="16 ";} ID {
		int index = tokens.getLastIndex();
		Symbol id = SymbolTable.find(tablaSA,tablaSG, $ID.text);
		Tipo idTipo = id == null? Tipo.INT : id.getTipo();
		} sp[idTipo] {
		if(id == null && !$sp.spSymbol.isFunction()){
			id = new Symbol(Tipo.INT,desplG);
			id.setLexema($ID.text);
			$sSymbol.setTipo(Tipo.INT);
			tokens.setLastToken(index,tablaSG.insertar($ID.text, id));
			desplG += Tipo.INT.getSize();
			desplA += tablaSG == tablaSA? Tipo.INT.getSize() : 0;
		} else if (id != null && id.isFunction() && $sp.spSymbol.isFunction()){
			tokens.setLastToken(index,SymbolTable.findPos(tablaSA,tablaSG,$ID.text));
			if(id.getParam().equals($sp.spSymbol.getParam())) {
				$sSymbol.setTipo(id.getRetorno());
			} else {
				notifyErrorListeners(Error.ERROR_PARAMETROS_FUNC.getMsg());
			}
		} else if (id != null && !id.isFunction() && !$sp.spSymbol.isFunction()){
			tokens.setLastToken(index,SymbolTable.findPos(tablaSA,tablaSG,$ID.text));
			$sSymbol.setTipo(id.getTipo());
		} else {
			notifyErrorListeners(Error.ERROR_NO_FUNC.getMsg());
		}
	}
	| {parse+="20 ";} PUT e SEMICOLON {
		if($e.eSymbol.getTipo() != Tipo.INT && $e.eSymbol.getTipo() != Tipo.STRING) {
			notifyErrorListeners(Error.ERROR_E_INVALIDO_PUT.getMsg());
		}
	}
	| {parse+="21 ";} GET ID {int index = tokens.getLastIndex();
		Symbol id = SymbolTable.find(tablaSA,tablaSG, $ID.text);
			if(id == null){
				id = new Symbol(Tipo.INT,desplG);
				id.setLexema($ID.text);
				$sSymbol.setTipo(Tipo.INT);
				tokens.setLastToken(index,tablaSG.insertar($ID.text, id));
				desplG += Tipo.INT.getSize();
				desplA += tablaSG == tablaSA? Tipo.INT.getSize() : 0;
			} else {
				tokens.setLastToken(index,SymbolTable.findPos(tablaSA,tablaSG,$ID.text));
				if(id.getTipo() != Tipo.INT && id.getTipo() != Tipo.STRING) 
					notifyErrorListeners(Error.ERROR_E_INVALIDO_GET.getMsg());
			}
	} SEMICOLON
	| {parse+="22 ";} RETURN x {
		if(tablaSG == tablaSA)
			notifyErrorListeners(Error.ERROR_RETURN_TSG.getMsg());
		if(!tablaSA.checkReturn($x.xSymbol.getRetorno()))
			notifyErrorListeners(Error.ERROR_RETURN_TIPO.getMsg());
		} SEMICOLON;

sp[Tipo spTipo]
	returns[Symbol spSymbol]
	@init {$spSymbol = new Symbol();}:
	{parse+="17 ";} ASSIGN e {
		if ($e.eSymbol.getTipo() != spTipo) {
			notifyErrorListeners(Error.ERROR_ASIGN.getMsg());
		}
	 	$spSymbol.setFunction(false);
		} SEMICOLON
	| {parse+="18 ";} AND_EQ e {
		if ($e.eSymbol.getTipo() != Tipo.INT || spTipo != Tipo.INT) {
			notifyErrorListeners(Error.ERROR_BIT_AND.getMsg());
		}
	 	$spSymbol.setFunction(false);
		} SEMICOLON
	| {parse+="19 ";} L_PARENT l {
	 	$spSymbol.setFunction(true);
		$spSymbol.setParam($l.lSymbol.getParam());
		} R_PARENT SEMICOLON;

l
	returns[Symbol lSymbol]
	@init {$lSymbol = new Symbol();}:
	{parse+="23 ";} e q { List<Tipo> qParam = $q.qSymbol.getParam();
		List<Tipo> lParam = $lSymbol.getParam();
		if(qParam.size() == 0) {
			lParam.add($e.eSymbol.getTipo());
		} else {
			lParam.add($e.eSymbol.getTipo());
			lParam.addAll(qParam);
		}
	}
	| {parse+="24 ";};

q
	returns[Symbol qSymbol]
	@init {$qSymbol = new Symbol();}:
	{parse+="25 ";} COMMA e q { List<Tipo> q1Param = $q.qSymbol.getParam();
		List<Tipo> qParam = $qSymbol.getParam();
		if(q1Param.size() == 0) {
			qParam.add($e.eSymbol.getTipo());
		} else {
			qParam.add($e.eSymbol.getTipo());
			qParam.addAll(q1Param);
		}
	}
	| {parse+="26 ";};

x
	returns[Symbol xSymbol]
	@init {$xSymbol = new Symbol();}:
	{parse+="27 ";} e {$xSymbol.setRetorno($e.eSymbol.getTipo());}
	| {parse+="28 "; $xSymbol.setRetorno(Tipo.VACIO);};

b:
	{parse+="29 ";} s
	| {parse+="30 ";} IF L_PARENT e {if( $e.eSymbol.getTipo() != Tipo.BOOL) {
		notifyErrorListeners(Error.ERROR_E_IF.getMsg());
		}} R_PARENT bp
	| {parse+="33 ";} LET ID {int index = tokens.getLastIndex();} t {
		if(tablaSA.get($ID.text) != null) {
			notifyErrorListeners(Error.ERROR_ID_DECLARADO.getMsg());
		}
		Tipo tTipo = $t.tSymbol.getTipo();
		Symbol id = new Symbol(tTipo,desplA);
		id.setLexema($ID.text);
		tokens.setLastToken(index,tablaSA.insertar($ID.text, id));
		desplA += tTipo.getSize();
		desplG += tablaSG == tablaSA? tTipo.getSize() : 0;
	} SEMICOLON;

bp: {parse+="31 ";} s | {parse+="32 ";} L_BRACE c R_BRACE w;

w: {parse+="34 ";} ELSE L_BRACE c R_BRACE | {parse+="35 ";};

t
	returns[Symbol tSymbol]
	@init {$tSymbol = new Symbol();}:
	{parse+="36 ";} INT {$tSymbol.setTipo(Tipo.INT);}
	| {parse+="37 ";} BOOLEAN {$tSymbol.setTipo(Tipo.BOOL);}
	| {parse+="38 ";} STRING {$tSymbol.setTipo(Tipo.STRING);};

f:
	{parse+="39 ";} FUNCTION ID { Symbol id = tablaSG.get($ID.text);
	int index = tokens.getLastIndex();
	if (id != null) {
		notifyErrorListeners(Error.ERROR_ID_DECLARADO.getMsg());
	}
		desplA = 0; 
		tablaSA = new SymbolTable(new Symbol());
	} h L_PARENT a { Symbol func = new Symbol(Tipo.FUNCTION,0);
		func.setLexema($ID.text);
		func.setRetorno($h.hSymbol.getTipo());
		func.setParam($a.aSymbol.getParam());
		func.setETFuncion("Et" + tablas.size() + "_" + $ID.text);
		func.setFunction(true);
		tokens.setLastToken(index,tablaSG.insertar($ID.text, func));
		tablaSA.setFuncSymbol(func);
	} R_PARENT L_BRACE c R_BRACE {
		tablas.add(tablaSA);
		tablaSA = tablaSG;
		desplA = desplG;};

h
	returns[Symbol hSymbol]
	@init {$hSymbol = new Symbol();}:
	{parse+="40 ";} t {$hSymbol.setTipo($t.tSymbol.getTipo());}
	| {parse+="41 ";} VOID {$hSymbol.setTipo(Tipo.VACIO);};

a
	returns[Symbol aSymbol]
	@init {$aSymbol = new Symbol();}:
	{parse+="42 ";} t ID {
		if(tablaSA.get($ID.text) != null) {
			notifyErrorListeners(Error.ERROR_PARAMETRO_DECLARADO.getMsg());
		}
		int index = tokens.getLastIndex();
		Tipo tTipo = $t.tSymbol.getTipo();
		Symbol id = new Symbol(tTipo,desplA);
		id.setLexema($ID.text);
		tokens.setLastToken(index,tablaSA.insertar($ID.text, id));
		desplA += tTipo.getSize();
		} k { List<Tipo> kParam = $k.kSymbol.getParam();
		List<Tipo> aParam = $aSymbol.getParam();
		if(kParam.size() == 0) {
			aParam.add($t.tSymbol.getTipo());
		} else {
			aParam.add($t.tSymbol.getTipo());
			aParam.addAll(kParam);
		}
	}
	| {parse+="43 ";} VOID;

k
	returns[Symbol kSymbol]
	@init {$kSymbol = new Symbol();}:
	{parse+="44 ";} COMMA t ID {
		if(tablaSA.get($ID.text) != null) {
			notifyErrorListeners(Error.ERROR_PARAMETRO_DECLARADO.getMsg());
		}
		int index = tokens.getLastIndex();
		Tipo tTipo = $t.tSymbol.getTipo();
		Symbol id = new Symbol(tTipo,desplA);
		id.setLexema($ID.text);
		tokens.setLastToken(index,tablaSA.insertar($ID.text, id));
		desplA += tTipo.getSize();
		} k { List<Tipo> k1Param = $k.kSymbol.getParam();
		List<Tipo> kParam = $kSymbol.getParam();
		if(k1Param.size() == 0) {
			kParam.add(tTipo);
		} else {
			kParam.add(tTipo);
			kParam.addAll(k1Param);
		}
	}
	| {parse+="45 ";};

c: {parse+="46 ";} b c | {parse+="47 ";};

p: {parse+="48 ";} b p | {parse+="49 ";} f p | {parse+="50 ";} EOF;

//Lexer
INT: 'int';
LET: 'let';
RETURN: 'return';
IF: 'if';
ELSE: 'else';
BOOLEAN: 'boolean';
TRUE: 'true';
FALSE: 'false';
GET: 'get';
FUNCTION: 'function';
PUT: 'put';
STRING: 'string';
VOID: 'void';

INT_LITERAL: [0-9]+;
STRING_LITERAL: '"' (~('\n' | '\r'))*? '"';
ID: [_a-zA-Z][_a-zA-Z0-9]*;

SEMICOLON: ';';
L_BRACE: '{';
R_BRACE: '}';
L_PARENT: '(';
R_PARENT: ')';
COMMA: ',';

ASSIGN: '=';
NOT: '!';
ADD: '+';
NOT_EQ: '!=';
AND_EQ: '&=';

WS: [ \t\r\n]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;