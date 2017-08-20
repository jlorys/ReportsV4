import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {Message, SelectItem} from "primeng/primeng";
import {ActivatedRoute, Router} from "@angular/router";
import {MessageService} from "../../service/message.service";
import {AppUser} from "./user";
import {AppUserDataService} from "./user.data.service";
import {Role} from "../role/role";
import {RoleDataService} from "../role/role.data.service";
import {ReportDataService} from "../report/report.data.service";
import {Report} from "../report/report";

@Component({
  moduleId: module.id,
  templateUrl: 'user.add.component.html',
  selector: 'users-add',
})
export class AppUsersAddComponent implements OnInit, OnDestroy {
  user : AppUser;

  private params_subscription: any;

  sourceRoles : Role[] = [];
  sourceReports : Report[] = [];

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<AppUser>();
  @Output() onCancelClicked = new EventEmitter();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private userService: AppUserDataService, private roleService : RoleDataService, private reportService: ReportDataService) {
    roleService.complete(null).
    subscribe(roles => this.sourceRoles = roles,
      error => this.msgs.push({severity:'error', summary:'Constructor user roles error', detail: error}))

    reportService.complete(null).
    subscribe(roles => this.sourceReports = roles,
      error => this.msgs.push({severity:'error', summary:'Constructor user reports error', detail: error}))
  }

  ngOnInit() {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('ngOnInit for users-add ' + id);
      if (id === 'add') {
        this.user = new AppUser();
      } else {
        this.userService.getUser(id)
          .subscribe(user => {
              this.user = user;
              this.sourceRoles = this.sourceRoles.filter(item => this.user.roles.map((e) => e.id).indexOf(item.id) < 0);
              this.sourceReports = this.sourceReports.filter(item => this.user.reports.map((e) => e.id).indexOf(item.id) < 0);
            },
            error => this.msgs.push({severity:'error', summary:'ngOnInit error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onSave() {
    this.userService.update(this.user).
    subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.user);
          this.msgs.push({severity:'info', summary:'Saved OK and msg emitted', detail: 'Angular Rocks!'})
        } else {
          this.msgs.push({severity:'info', summary:'Saved OK', detail: 'Angular Rocks!'})

        }
      },
      error => this.msgs.push({severity:'error', summary:'Could not save', detail: 'Angular Rocks!'})
    );
  }

  onCancel() {
    if (this.sub) {
      this.msgs = []; //this line fix disappearing of messages
      this.onCancelClicked.emit("cancel");
      this.msgs.push({severity:'info', summary:'Cancel clicked and msg emitted', detail: 'Angular Rocks!'})
    }
  }
}
