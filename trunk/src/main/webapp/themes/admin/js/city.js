


var Provinces = document.getElementById('provinces');
var Cities = document.getElementById('cities');

function Location(Province, City)
{
	this.Province	= Province;
	this.City		= City;
}

// Construct the location data
var arrLocation = new Array(35);
arrLocation[0]  = new Location('��ѡ��', ''); 
arrLocation[1]	= new Location('����', '������|������|������|������|������|������|��̨��|ʯ��ɽ');
arrLocation[2]	= new Location('�Ϻ�', '��ɽ|��ɽ|����|����|����|����|����|¬��|�ɽ�|����|�ֶ�|����|���|����|բ��|����|����|���|�ζ�|�ϻ�');
arrLocation[3]	= new Location('���', '��ƽ|�ӱ�|����|�Ӷ�|�Ͽ�|����|����|����|���|����|����|����|����|����|����');
arrLocation[4]	= new Location('����', '����|����|ɳƺ��|�ϰ�|������|��ɿ�');
arrLocation[5]	= new Location('�ӱ�', 'ʯ��ׯ|����|����|�żҿ�|�е�|��ɽ|�ȷ�|����|��ˮ|��̨|�ػʵ�');
arrLocation[6]	= new Location('ɽ��', '̫ԭ|����|��ͬ|����|����|�ٷ�|����|˷��|����|��Ȫ|�˳�');
arrLocation[7]	= new Location('���ɹ�', '���ͺ���|������|�����׶���|��ͷ|���|������˹|���ױ���|����������|ͨ��|�ں�|���ֹ�����|�˰���|�����첼');
arrLocation[8]	= new Location('����', '����|��ɽ|��Ϫ|����|����|����|��˳|����|��«��|����|����|�̽�|����|Ӫ��');
arrLocation[9]	= new Location('����', '����|����|�׳�|��ɽ|��Դ|��ƽ|��ԭ|ͨ��|�ӱ���');
arrLocation[10]	= new Location('������', '������|����|���˰���|�׸�|�ں�|����|��ľ˹|ĵ����|��̨��|�������|˫Ѽɽ|�绯|����');
arrLocation[11]	= new Location('����', '�Ͼ�|����|����|����|���Ƹ�|��ͨ|��Ǩ|̩��|����|����|�γ�|����|��');
arrLocation[12]	= new Location('�㽭', '����|����|����|����|��|��ˮ|����|����|����|̨��|����|��ɽ');
arrLocation[13]	= new Location('����', '�Ϸ�|����|�ߺ�|����|����|��ɽ|����|����|����|����|����|����|����|��ɽ|����|ͭ��|����');
arrLocation[14]	= new Location('����', '����|����|����|����|Ȫ��|����|����|����|��ƽ');
arrLocation[15]	= new Location('����', '�ϲ�|����|����|����|������|�Ž�|Ƽ��|����|����|�˴�|ӥ̶');
arrLocation[16]	= new Location('ɽ��', '����|����|�ൺ|�Ͳ�|����|��̨|Ϋ��|����|̩��|����|����|��Ӫ|����|��ׯ|����|����|�ĳ�');
arrLocation[17]	= new Location('����', '֣��|����|�ױ�|����|����|����|���|����|ƽ��ɽ|���|����Ͽ|����|����|����|���|�ܿ�|פ���|��Դ');
arrLocation[18]	= new Location('����', '�人|����|�Ƹ�|��ʯ|ʮ��|����|����|�差|Т��|�˲�|����|����|��ʩ|����');
arrLocation[19]	= new Location('����', '��ɳ|����|����|����|����|¦��|����|��̶|����|����|����|�żҽ�|����|������');
arrLocation[20]	= new Location('�㶫', '����|��β|����|����|ï��|����|�ع�|����|÷��|��ͷ|����|�麣|��ɽ|����|տ��|��ɽ|��Զ|�Ƹ�|����|��ݸ|��Դ');
arrLocation[21]	= new Location('����', '����|����|����|����|����|���|��ɫ|����|���Ǹ�|�ӳ�|����|����|����|����');
arrLocation[22]	= new Location('����', '����|����|��ָɽ|��|����|�Ĳ�|����|����|ͨʲ');
arrLocation[23]	= new Location('�Ĵ�', '�ɶ�|������|����|����|����|�㰲|��Ԫ|��ɽ|��ɽ|����|üɽ|����|�ڽ�|�ϳ�|��֦��|����|�Ű�|�˱�|����|�Թ�|������');
arrLocation[24]	= new Location('����', '����|����|��˳|ǭ��|ǭ����|ͭ��|�Ͻ�|����ˮ|ǭ����');
arrLocation[25]	= new Location('����', '����|��ɽ|����|����|�º�|����|���|����|�ٲ�|ŭ��|����|��ɽ|��˫����|��Ϫ|��ͨ|�ն�');
arrLocation[26]	= new Location('����', '����|�տ���|��֥|����|����|����|ɽ��');
arrLocation[27]	= new Location('����', '����|����|����|����|����|ͭ��|μ��|����|�Ӱ�|����');
arrLocation[28]	= new Location('����', '����|ƽ��|��Ҵ|��Ȫ|������|��ˮ|����|����|���ϲ���������|���|����|¤��|����|����');
arrLocation[29]	= new Location('����', '����|��ԭ|ʯ��ɽ|����|����');
arrLocation[30]	= new Location('�ຣ', '����|����|��������|������|������|������|������|������');
arrLocation[31]	= new Location('�½�', '��³ľ��|����|����|����̩|��������|ʯ����|����|��³��|������|��ʲ|����|�������տ¶�����|��������|��������');
arrLocation[32]	= new Location('���', '������');
arrLocation[33]	= new Location('����', '������');
arrLocation[34]	= new Location('̨��', '̨��|����|̨��|̨��|��¡|�û�|����|����|̨��|����|����');
arrLocation[35]	= new Location('����', '����');

/*
 * ִ�к���
 */
function selectedCity(cit)
{
	
	var selected = Provinces.options[Provinces.selectedIndex].value;
	if(isNaN(selected)){
		 selected = Provinces.selectedIndex;
	}
	Provinces.options[Provinces.selectedIndex].value = Provinces.options[Provinces.selectedIndex].text;
	
	if (Cities!="null"){
		var arrCities = (arrLocation[selected].City).split("|");
		Cities.length = arrCities.length;
		for(var i = 0; i < arrCities.length; i++) {
			if ( cit==arrCities[i]){
				Cities.options[i].selected = 'selected';
			}
			Cities.options[i].text	= arrCities[i];
			Cities.options[i].value	= arrCities[i];
		}
	}
}   

function ProvinceCity(val,cit){
	Provinces.length = arrLocation.length;
	for (var i = 0; i < arrLocation.length; i++) {
		Provinces.options[i].text = arrLocation[i].Province;
		if (arrLocation[i].Province==val){
			Provinces.options[i].selected = 'selected';
			Provinces.options[i].value = arrLocation[i].Province;
			var j=i;
		}else{
			Provinces.options[i].value = i;
		}
	}
	selectedCity(cit);
}

function Province(val){
	Provinces.length = arrLocation.length;
	for (var i = 0; i < arrLocation.length; i++) {
		Provinces.options[i].text = arrLocation[i].Province;
		Provinces.options[i].value = arrLocation[i].Province;
		if (arrLocation[i].Province==val){
			Provinces.options[i].selected = 'selected';
		}
	}
}