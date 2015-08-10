using System;
namespace QR.Model
{
	/// <summary>
	/// 1
	/// </summary>
	[Serializable]
	public partial class UserTable
	{
		public UserTable()
		{}
		#region Model
		private int _user_id;
		private string _username;
		private string _password;
		private string _usertype;
		private string _status;
		private DateTime? _lastlogintime;
		private DateTime? _lastexittime;
		/// <summary>
		/// 
		/// </summary>
		public int User_id
		{
			set{ _user_id=value;}
			get{return _user_id;}
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
		public string Password
		{
			set{ _password=value;}
			get{return _password;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string UserType
		{
			set{ _usertype=value;}
			get{return _usertype;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string Status
		{
			set{ _status=value;}
			get{return _status;}
		}
		/// <summary>
		/// 
		/// </summary>
		public DateTime? LastLoginTime
		{
			set{ _lastlogintime=value;}
			get{return _lastlogintime;}
		}
		/// <summary>
		/// 
		/// </summary>
		public DateTime? LastExitTime
		{
			set{ _lastexittime=value;}
			get{return _lastexittime;}
		}
		#endregion Model

	}
}

