using System;
namespace QR.Model
{
	/// <summary>
	/// Location:实体类(属性说明自动提取数据库字段的描述信息)
	/// </summary>
	[Serializable]
	public partial class Location
	{
		public Location()
		{}
		#region Model
		private int _location_id;
		private string _username;
		private decimal? _latitude;
		private decimal? _longitude;
		private DateTime? _time;
		/// <summary>
		/// 
		/// </summary>
		public int Location_id
		{
			set{ _location_id=value;}
			get{return _location_id;}
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
		public decimal? Latitude
		{
			set{ _latitude=value;}
			get{return _latitude;}
		}
		/// <summary>
		/// 
		/// </summary>
		public decimal? Longitude
		{
			set{ _longitude=value;}
			get{return _longitude;}
		}
		/// <summary>
		/// 
		/// </summary>
		public DateTime? Time
		{
			set{ _time=value;}
			get{return _time;}
		}
		#endregion Model

	}
}

