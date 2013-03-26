package com.divrep.i18n;

import java.io.Serializable;
import java.util.Locale;

public abstract class Labels implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -744728301904577948L;
	static Labels instance = null;
	static public Labels getInstance() {
		if(instance == null) {
			initInstance(Locale.getDefault());	
		}
		return instance;
	}
	static private void initInstance(Locale locale) {

		if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
			instance = new Labels_FRENCH();
		} else if (locale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
			instance = new Labels_JAPANESE();
		} else if (locale.getLanguage().equals("tr")) {
			instance = new Labels_TURKISH();
		} else if (locale.getLanguage().equals("es")) {
			instance = new Labels_SPANISH();
		} else {
			instance = new Labels_ENGLISH();
		}
	}
	
	public abstract String RequiredFieldMessage();
	public abstract String RequiredFieldNote();
	public abstract String PleaseCorrectAndResubmit();
	public abstract String Cancel();
	public abstract String Submit();
	public abstract String OK();
	public abstract String Loading();
	public abstract String ShowDetail();
	public abstract String HideDetail();
	public abstract String InvalidDateFormat();
}

class Labels_ENGLISH extends Labels {
	public String RequiredFieldMessage() { return "This is a required field. Please specify a value."; }
	public String RequiredFieldNote() { return " * Required"; }
	public String PleaseCorrectAndResubmit() { return "Please correct the issues flagged above, and then resubmit!"; }
	public String Cancel() { return "Cancel"; }
	public String Submit() { return "Submit"; }
	public String OK()  { return "OK"; }
	public String Loading() {return "Loading..."; }
	public String HideDetail() { return "Hide Detail"; }
	public String ShowDetail() { return "Show Detail"; }
	public String InvalidDateFormat() { return "Please specify a valid date.";}
}

class Labels_JAPANESE extends Labels {
	public String RequiredFieldMessage() { return "値を指定して下さい。"; }
	public String RequiredFieldNote() { return " * 必要なフィールド"; }
	public String PleaseCorrectAndResubmit() { return "問題な箇所を修正し、もう一度送信してください。"; }
	public String Cancel() { return "キャンセル"; }
	public String Submit() { return "送信"; }
	public String OK()  { return "OK"; }
	public String Loading() {return "読み込み中..."; }
	public String HideDetail() { return "非表示"; }
	public String ShowDetail() { return "表示"; }
	public String InvalidDateFormat() { return "正しい日時をいれてください。";}
}

class Labels_FRENCH extends Labels {
	public String RequiredFieldMessage() { return "Ce champ est obligatoire. Veuillez saisir une valeur"; }
	public String RequiredFieldNote() { return " * Requis"; }
	public String PleaseCorrectAndResubmit() { return "Veuillez corriger les erreurs, et valider à nouveau."; }
	public String Cancel() { return "Annuler"; }
	public String Submit() { return "Valider"; }
	public String OK()  { return "OK"; }
	public String Loading() {return "Chargement..."; }
	public String HideDetail() { return "Cacher les détails"; }
	public String ShowDetail() { return "Montrer les détails"; }
	public String InvalidDateFormat() { return "Veuillez entrer une date correcte.";}
}

class Labels_TURKISH extends Labels {
    public String RequiredFieldMessage() { return "Bu gerekli bir alandır. Lütfen bir değer belirtiniz."; }
    public String RequiredFieldNote() { return " * Gerekli"; }
    public String PleaseCorrectAndResubmit() { return "Lütfen yukarıdaki işaretli noktaları düzeltiniz ve yeniden gönderiniz."; }
    public String Cancel() { return "İptal"; }
    public String Submit() { return "Gönder"; }
    public String OK()  { return "Tamam"; }
    public String Loading() {return "Yükleniyor..."; }
    public String HideDetail() { return "Detayları Gizle"; }
    public String ShowDetail() { return "Detayları Göster"; }
    public String InvalidDateFormat() { return "Lütfen geçerli bir tarih giriniz.";}
}

class Labels_SPANISH extends Labels {
    public String RequiredFieldMessage() { return "Este campo debe estar completo. Por favor indique un valor."; }
    public String RequiredFieldNote() { return " * Necesario"; }
    public String PleaseCorrectAndResubmit() { return "Por favor corrija los items marcados y vuelva a intentarlo!"; }
    public String Cancel() { return "Cancelar"; }
    public String Submit() { return "Aceptar"; }

    public String OK()  { return "OK"; }
    public String Loading() {return "Cargando..."; }
    public String HideDetail() { return "Ocultar detalle"; }
    public String ShowDetail() { return "Mostrar detalle"; }
    public String InvalidDateFormat() { return "Por favor, indique una fecha valida.";}
}
