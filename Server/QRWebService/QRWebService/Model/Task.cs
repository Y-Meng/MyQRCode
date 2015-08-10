using System;
namespace QR.Model
{
	/// <summary>
	/// 1
	/// </summary>
	[Serializable]
	public partial class Task
	{
		public Task()
		{}
		#region Model
		private int _taskid;
		private string _tasktype;
		private string _username;
		private string _description;
		private DateTime? _time;
		private string _taskstatus;
		private string _taskdetail;
		/// <summary>
		/// 
		/// </summary>
		public int TaskID
		{
			set{ _taskid=value;}
			get{return _taskid;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string TaskType
		{
			set{ _tasktype=value;}
			get{return _tasktype;}
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
		public string Description
		{
			set{ _description=value;}
			get{return _description;}
		}
		/// <summary>
		/// 
		/// </summary>
		public DateTime? Time
		{
			set{ _time=value;}
			get{return _time;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string TaskStatus
		{
			set{ _taskstatus=value;}
			get{return _taskstatus;}
		}
		/// <summary>
		/// 
		/// </summary>
		public string TaskDetail
		{
			set{ _taskdetail=value;}
			get{return _taskdetail;}
		}
		#endregion Model

	}
}

