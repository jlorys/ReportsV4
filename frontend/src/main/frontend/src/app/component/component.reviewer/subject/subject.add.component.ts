import {Component} from "@angular/core";
import {SubjectAddComponent} from "../../component.admin/subject/subject.add.component";

@Component({
  selector: 'reviewerSubjects-add',
  templateUrl: '../../component.admin/subject/subject.add.component.html',
})
export class ReviewerSubjectAddComponent extends SubjectAddComponent{

  onRowSelect(event: any) {
    let id = event.data.id;
    this.router.navigate(['/reviewerLaboratories', id]);
  }
}
