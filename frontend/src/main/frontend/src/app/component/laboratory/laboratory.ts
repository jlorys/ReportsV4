import {Subject} from "../subject/subject";
import {Report} from "../report/report";

export class Laboratory {

  id: number;
  name: string;
  description: string;
  labDate: number;
  returnReportDate: number;
  finalReturnReportDate: number;
  createdDate: number;
  lastModifiedDate: number;
  createdBy: string;
  lastModifiedBy: string;
  subject: Subject;
  reports: Report[];

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
      this.description = json.description;
      this.labDate = json.labDate;
      this.returnReportDate = json.returnReportDate;
      this.finalReturnReportDate = json.finalReturnReportDate;
      this.createdDate = json.createdDate;
      this.lastModifiedDate = json.lastModifiedDate;
      this.createdBy = json.createdBy;
      this.lastModifiedBy = json.lastModifiedBy;
      this.subject = json.subject;
      if (json.reports != null) {
        this.reports = Report.toArray(json.reports);
      }
    }
  }

  // Utils
  static toArray(jsons : any[]) : Laboratory[] {
    let laboratories : Laboratory[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        laboratories.push(new Laboratory(json));
      }
    }
    return laboratories;
  }
}
