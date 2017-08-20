import {AppUser} from "../users/user";

export class Report {

  id: number;
  description: string;
  filePath: string;
  fileName: string;
  fileExtension: string;
  grade: string;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: string;
  lastModifiedBy: string;
  isSendInTime: boolean;
  users : AppUser[];

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.description = json.description;
      this.filePath = json.filePath;
      this.fileName = json.fileName;
      this.fileExtension = json.fileExtension;
      this.grade = json.grade;
      this.createdDate = json.createdDate;
      this.lastModifiedDate = json.lastModifiedDate;
      this.createdBy = json.createdBy;
      this.lastModifiedBy = json.lastModifiedBy;
      this.isSendInTime = json.isSendInTime;
      if (json.users != null) {
        this.users = AppUser.toArray(json.roles);
      }
    }
  }

  // Utils
  static toArray(jsons : any[]) : Report[] {
    let reports : Report[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        reports.push(new Report(json));
      }
    }
    return reports;
  }
}
