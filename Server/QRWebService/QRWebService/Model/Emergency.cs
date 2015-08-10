using System;
namespace QR.Model
{
	/// <summary>
	/// Emergency:实体类(属性说明自动提取数据库字段的描述信息)
	/// </summary>
	[Serializable]
	public partial class Emergency
	{
		public Emergency()
		{}
		#region Model
		private int _emergency_id;
		private int _event_id;
		private string _username;
		private string _eventstatus;
		private string _note;
		private byte[] _pic;
		private string _vedioname;
		private byte[] _vedio;
		private string _audioname;
		private byte[] _audio;
		private DateTime _time;
		/// <summary>
		/// 
		/// </summary>
		public int Emergency_id
		{
			set{ _emergency_id=value;}
			get{return _emergency_id;}
		}
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
		public string EventStatus
		{
			set{ _eventstatus=value;}
			get{return _eventstatus;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string Note
		{
			set{ _note=value;}
			get{return _note;}
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
		public string VedioName
		{
			set{ _vedioname=value;}
			get{return _vedioname;}
		}
		/// <summary>
		/// 
		/// </summary>
		public byte[] Vedio
		{
			set{ _vedio=value;}
			get{return _vedio;}
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
		/// <summary>
		/// 
		/// </summary>
		public DateTime Time
		{
			set{ _time=value;}
			get{return _time;}
		}
		#endregion Model

	}
}

