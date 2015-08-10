using System;
namespace QR.Model
{
	/// <summary>
	/// Event:实体类(属性说明自动提取数据库字段的描述信息)
	/// </summary>
	[Serializable]
	public partial class Event
	{
		public Event()
		{}
		#region Model
		private int _event_id;
		private string _username;
		private string _qrcode;
		private string _qrdetail;
		private string _qrstatus;
		private DateTime _time;
		private byte[] _pic;
		private string _picname;
		private string _videoname;
		private byte[] _video;
		private string _audioname;
		private byte[] _audio;
		/// <summary>
		/// 
		/// </summary>
		public int Event_id
		{
			set{ _event_id=value;}
			get{return _event_id;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string UserName
		{
			set{ _username=value;}
			get{return _username;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string QRcode
		{
			set{ _qrcode=value;}
			get{return _qrcode;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string QRdetail
		{
			set{ _qrdetail=value;}
			get{return _qrdetail;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string QRstatus
		{
			set{ _qrstatus=value;}
			get{return _qrstatus;}
		}
		/// <summary>
		/// 
		/// </summary>
		public DateTime Time
		{
			set{ _time=value;}
			get{return _time;}
		}
		/// <summary>
		/// 
		/// </summary>
		public byte[] Pic
		{
			set{ _pic=value;}
			get{return _pic;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string PicName
		{
			set{ _picname=value;}
			get{return _picname;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string VideoName
		{
			set{ _videoname=value;}
			get{return _videoname;}
		}
		/// <summary>
		/// 
		/// </summary>
		public byte[] Video
		{
			set{ _video=value;}
			get{return _video;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string AudioName
		{
			set{ _audioname=value;}
			get{return _audioname;}
		}
		/// <summary>
		/// 
		/// </summary>
		public byte[] Audio
		{
			set{ _audio=value;}
			get{return _audio;}
		}
		#endregion Model

	}
}

