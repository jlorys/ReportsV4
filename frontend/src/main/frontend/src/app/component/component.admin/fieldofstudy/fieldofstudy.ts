import {Subject} from "../subject/subject";

export class FieldOfStudy {

  id: number;
  name: string;
  description: string;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: string;
  lastModifiedBy: string;
  subjects : Subject[];

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
      this.description = json.description;
      this.createdDate = json.createdDate;
      this.lastModifiedDate = json.lastModifiedDate;
      this.createdBy = json.createdBy;
      this.lastModifiedBy = json.lastModifiedBy;
      if (json.subjects != null) {
        this.subjects = Subject.toArray(json.subjects);
      }
    }
  }

  // Utils
  static toArray(jsons : any[]) : FieldOfStudy[] {
    let fieldsofstudies : FieldOfStudy[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        fieldsofstudies.push(new FieldOfStudy(json));
      }
    }
    return fieldsofstudies;
  }
}
